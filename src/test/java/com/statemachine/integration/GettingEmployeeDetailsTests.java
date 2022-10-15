package com.statemachine.integration;

import com.statemachine.StatemachineApplicationTests;
import com.statemachine.core.dto.payload.request.AddingEmployeeRequest;
import com.statemachine.core.dto.payload.response.EmployeeDetailsResponse;
import com.statemachine.util.RequestBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.BDDAssertions.then;

class GettingEmployeeDetailsTests extends StatemachineApplicationTests {

    private final static String PATH = "/v1/employees/{employeeId}";

    @Test
    void getEmployee_successfully() {

        AddingEmployeeRequest request = RequestBuilder.buildAddingEmployeeRequest();

        EmployeeDetailsResponse employee = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post("/v1/employees")
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().as(EmployeeDetailsResponse.class);

        EmployeeDetailsResponse response = given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("employeeId", employee.getId())
                .when().get(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value())
                .extract().as(EmployeeDetailsResponse.class);

        then(response).isNotNull();
        then(response.getId()).isNotNull();
        then(response.getName()).isNotNull();
        then(response.getCurrentStatusList()).isNotNull();

        then(response.getContract().getId()).isNotNull();
        then(response.getContract().getDuration()).isNotNull();
        then(response.getContract().getSalary()).isNotNull();
        then(response.getContract().getStartDate()).isNotNull();
    }

    @Test
    void getEmployeeNotExistEmployee_shouldThrowsException() {

        given().contentType(ContentType.JSON).accept(ContentType.JSON)
                .pathParam("employeeId", 1L)
                .when().get(PATH)
                .then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());

    }
}
