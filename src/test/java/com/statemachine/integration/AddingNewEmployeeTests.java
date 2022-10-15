package com.statemachine.integration;

import com.statemachine.StatemachineApplicationTests;
import com.statemachine.core.dto.payload.request.AddingEmployeeRequest;
import com.statemachine.core.dto.payload.response.EmployeeDetailsResponse;
import com.statemachine.core.dal.model.Employee;
import com.statemachine.core.dal.repository.EmployeeRepository;
import com.statemachine.util.RequestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

import static com.statemachine.common.enums.EmployeeStatusEnum.ADDED;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.BDDAssertions.then;

class AddingNewEmployeeTests extends StatemachineApplicationTests {

    private final static String PATH = "/v1/employees";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void addEmployee_successfully() {

        AddingEmployeeRequest request = RequestBuilder.buildAddingEmployeeRequest();

        EmployeeDetailsResponse response = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post(PATH)
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().as(EmployeeDetailsResponse.class);

        then(response).isNotNull();
        then(response.getId()).isNotNull();
        then(response.getContract().getId()).isNotNull();

        Optional<Employee> employeeOption = employeeRepository.findById(response.getId());

        then(employeeOption).isNotNull();
        then(employeeOption.get().getId()).isNotNull();
        then(employeeOption.get().getContract().getId()).isNotNull();

        then(response.getCurrentStatusList()).isNotNull();
        then(response.getCurrentStatusList()).contains(ADDED.name());

    }

    @Test
    void addEmployeeRepeatedMail_throwValidationError() {

        AddingEmployeeRequest request = RequestBuilder.buildAddingEmployeeRequest();

        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post(PATH)
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().as(EmployeeDetailsResponse.class);

        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post(PATH)
                .then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());

    }

}
