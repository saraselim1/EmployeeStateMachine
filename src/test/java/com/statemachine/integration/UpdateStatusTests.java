package com.statemachine.integration;

import com.statemachine.StatemachineApplicationTests;
import com.statemachine.common.enums.StatusEventEnum;
import com.statemachine.core.dto.payload.request.AddingEmployeeRequest;
import com.statemachine.core.dto.payload.request.UpdateEmployeeStatusRequest;
import com.statemachine.core.dto.payload.response.EmployeeDetailsResponse;
import com.statemachine.util.RequestBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class UpdateStatusTests extends StatemachineApplicationTests {

    private final static String PATH = "/v1/employees/{employeeId}";

    /***
     * #### 🏃 Scenario 1 :
     * 1. create an employee
     * 2. Update state of the employee to `IN-CHECK`
     * 3. Update substate of `IN-CHECK` state of the employee to `SECURITY_CHECK_FINISHED`
     * 3. Update substate of `IN-CHECK` state of the employee to `WORK_PERMIT_CHECK_PENDING_VERIFICATION`
     * 4. Update substate of `IN-CHECK` state the employee to `WORK_PERMIT_CHECK_FINISHED` (employee is auto-transitioned to `APPROVED` state)
     * 5. Update state of the employee to `ACTIVE`
     ***/
    @Test
    void fullWorkFlow_successfully() {

        AddingEmployeeRequest request = RequestBuilder.buildAddingEmployeeRequest();

        EmployeeDetailsResponse employee = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post("/v1/employees")
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().as(EmployeeDetailsResponse.class);

        UpdateEmployeeStatusRequest inCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.BEGIN_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(inCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest securityCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_SECURITY_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(securityCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest initWorkPermitRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.COMPLETE_INITIAL_WORK_PERMIT_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(initWorkPermitRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest finishWorkPermitRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_WORK_PERMIT_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(finishWorkPermitRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest activateRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.ACTIVATE).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(activateRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

    }

    /***
     #### 🏃 Scenario 2 :
     1. create an employee
     2. Update state of the employee to `IN-CHECK`
     3. Update substate of `IN-CHECK` state the employee to `WORK_PERMIT_CHECK_PENDING_VERIFICATION`
     3. Update substate of `IN-CHECK` state the employee to `WORK_PERMIT_CHECK_FINISHED`
     4. Update substate of `IN-CHECK` state the employee to `SECURITY_CHECK_FINISHED` (employee is auto-transitioned to `APPROVED` state)
     5. Update state of the employee to `ACTIVE`
     ***/
    @Test
    void fullWorkFlowFinishWorkPermit_successfully() {

        AddingEmployeeRequest request = RequestBuilder.buildAddingEmployeeRequest();

        EmployeeDetailsResponse employee = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post("/v1/employees")
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().as(EmployeeDetailsResponse.class);

        UpdateEmployeeStatusRequest inCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.BEGIN_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(inCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest initWorkPermitRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.COMPLETE_INITIAL_WORK_PERMIT_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(initWorkPermitRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest finishWorkPermitRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_WORK_PERMIT_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(finishWorkPermitRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest securityCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_SECURITY_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(securityCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest activateRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.ACTIVATE).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(activateRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

    }

    /***
     #### 🏃 Scenario 3 :
     1. create an employee
     2. Update state of the employee to `IN-CHECK`
     3. Update substate of `IN-CHECK` state the employee to `WORK_PERMIT_CHECK_PENDING_VERIFICATION`
     4. Update substate of `IN-CHECK` state the employee to `SECURITY_CHECK_FINISHED`
     5. Update substate of `IN-CHECK` state the employee to `WORK_PERMIT_CHECK_FINISHED` (employee is auto-transitioned to `APPROVED` state)
     6. Update state of the employee to `ACTIVE`
     ***/
    @Test
    void fullWorkFinishSecurityCheckInMiddleOfPermitChecks_successfully() {

        AddingEmployeeRequest request = RequestBuilder.buildAddingEmployeeRequest();

        EmployeeDetailsResponse employee = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post("/v1/employees")
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().as(EmployeeDetailsResponse.class);

        UpdateEmployeeStatusRequest inCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.BEGIN_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(inCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest initWorkPermitRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.COMPLETE_INITIAL_WORK_PERMIT_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(initWorkPermitRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest securityCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_SECURITY_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(securityCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest finishWorkPermitRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_WORK_PERMIT_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(finishWorkPermitRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest activateRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.ACTIVATE).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(activateRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

    }

    /***
     * Unhappy path scenarios
     #### 💣 Scenario 1 :
     1. create an employee
     2. Update state of the employee to `IN-CHECK`
     3. Update substate of `IN-CHECK` state of the employee to `SECURITY_CHECK_FINISHED`
     4. Update state of the employee to `ACTIVE`: ❗✋transition `IN-CHECK` -> `ACTIVE` is not allowed
     ***/
    @Test
    void beginCheckAfterFinishSecurityCheck_shouldFail() {

        AddingEmployeeRequest request = RequestBuilder.buildAddingEmployeeRequest();

        EmployeeDetailsResponse employee = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post("/v1/employees")
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().as(EmployeeDetailsResponse.class);

        UpdateEmployeeStatusRequest inCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.BEGIN_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(inCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest securityCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_SECURITY_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(securityCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest inCheckSecondRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.BEGIN_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(inCheckSecondRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());

    }

    /***
     * Unhappy path scenarios
     #### 💣  Scenario 2 :
     1. create an employee
     2. Update state of the employee to `IN-CHECK`
     3. Update substate of `IN-CHECK` state the employee to `SECURITY_CHECK_FINISHED`
     4. Update substate of `IN-CHECK` state the employee to `WORK_PERMIT_CHECK_FINISHED`:
     ❗️✋transition `WORK_PERMIT_CHECK_STARTED` -> `WORK_PERMIT_CHECK_FINISHED` is not allowed
     ***/
    @Test
    void finishWorkPermitBeforeInit_shouldFail() {

        AddingEmployeeRequest request = RequestBuilder.buildAddingEmployeeRequest();

        EmployeeDetailsResponse employee = given().contentType(ContentType.JSON).accept(ContentType.JSON).body(request)
                .when().post("/v1/employees")
                .then().assertThat().statusCode(HttpStatus.CREATED.value())
                .extract().as(EmployeeDetailsResponse.class);

        UpdateEmployeeStatusRequest inCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.BEGIN_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(inCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest securityCheckRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_SECURITY_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(securityCheckRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.OK.value());

        UpdateEmployeeStatusRequest finishWorkPermitRequest =
                UpdateEmployeeStatusRequest.builder().event(StatusEventEnum.FINISH_WORK_PERMIT_CHECK).build();
        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(finishWorkPermitRequest)
                .pathParam("employeeId", employee.getId())
                .when().put(PATH)
                .then().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());

    }
}
