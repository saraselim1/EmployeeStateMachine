package com.statemachine.controller.employee.v1;

import com.statemachine.core.dto.payload.request.AddingEmployeeRequest;
import com.statemachine.core.dto.payload.request.UpdateEmployeeStatusRequest;
import com.statemachine.core.dto.payload.response.EmployeeDetailsResponse;
import com.statemachine.core.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping( produces = "application/json")
    public ResponseEntity<EmployeeDetailsResponse> addEmployee(@RequestBody @Valid AddingEmployeeRequest addingEmployeeRequest) {
        return new ResponseEntity<>(employeeService.addNewEmployee(addingEmployeeRequest),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Void> updateEmployeeStatus(@PathVariable Long id,
                                                                       @RequestBody @Valid UpdateEmployeeStatusRequest updateEmployeeStatusRequest) {
        employeeService.updateEmployeeStatus(id,updateEmployeeStatusRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EmployeeDetailsResponse> getEmployeeDetails(@PathVariable Long id) {
        return new ResponseEntity<>(employeeService.getEmployeeDetails(id),HttpStatus.OK);
    }

}
