package com.statemachine.core.service;

import com.statemachine.core.dto.payload.request.AddingEmployeeRequest;
import com.statemachine.core.dto.payload.request.UpdateEmployeeStatusRequest;
import com.statemachine.core.dto.payload.response.EmployeeDetailsResponse;

public interface EmployeeService {

    EmployeeDetailsResponse addNewEmployee(AddingEmployeeRequest addingEmployeeRequest);

    void updateEmployeeStatus(Long id, UpdateEmployeeStatusRequest updateEmployeeStatusRequest);

    EmployeeDetailsResponse getEmployeeDetails(Long id);
}
