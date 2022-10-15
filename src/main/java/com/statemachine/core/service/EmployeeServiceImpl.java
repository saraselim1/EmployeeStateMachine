package com.statemachine.core.service;

import com.statemachine.common.enums.EmployeeStatusEnum;
import com.statemachine.common.enums.StatusEventEnum;
import com.statemachine.core.dto.payload.request.AddingEmployeeRequest;
import com.statemachine.core.dto.payload.request.UpdateEmployeeStatusRequest;
import com.statemachine.core.dto.payload.response.EmployeeDetailsResponse;
import com.statemachine.core.mapper.EmployeeMapper;
import com.statemachine.core.dal.model.Employee;
import com.statemachine.core.dal.repository.CompanyRepository;
import com.statemachine.core.dal.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Log
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final StateMachineService stateMachineService;

    @Override
    public EmployeeDetailsResponse addNewEmployee(AddingEmployeeRequest addingEmployeeRequest) {

        validDate(addingEmployeeRequest);
        Employee newEmployee = EmployeeMapper.INSTANCE.toEntity(addingEmployeeRequest);
        newEmployee.setCurrentStatusList(Arrays.asList(EmployeeStatusEnum.ADDED.toString()));
        newEmployee.getContract().setEmployee(newEmployee);
        Employee savedEmployee = employeeRepository.save(newEmployee);
        List<StatusEventEnum> expectedEventList = buildExpectedEventList(savedEmployee);
        return EmployeeMapper.INSTANCE.toResponse(savedEmployee,expectedEventList);
    }

    @Override
    public void updateEmployeeStatus(Long id, UpdateEmployeeStatusRequest updateEmployeeStatusRequest) {
        Employee employee = getEmployee(id);
        StateMachine<EmployeeStatusEnum, StatusEventEnum> sm = stateMachineService.buildStateMachine(employee);
       if(!stateMachineService.sendEvent(employee, sm, updateEmployeeStatusRequest.getEvent()))
       {
           throw new ValidationException("Not allowed event.");
       }
    }

    @Override
    public EmployeeDetailsResponse getEmployeeDetails(Long id) {
        Employee employee = getEmployee(id);
        List<StatusEventEnum> expectedEventList = buildExpectedEventList(employee);
        return EmployeeMapper.INSTANCE.toResponse(employee, expectedEventList);
    }

    private List<StatusEventEnum> buildExpectedEventList(Employee employee) {
        StateMachine<EmployeeStatusEnum, StatusEventEnum> sm = stateMachineService.buildStateMachine(employee);
        return sm.getTransitions().stream()
                .filter(transition -> transition.getSource().getId().equals(sm.getState().getId()))
                .map(transition -> transition.getTrigger().getEvent())
                .collect(Collectors.toList());
    }

    private void validDate(AddingEmployeeRequest addingEmployeeRequest) {
        validateMail(addingEmployeeRequest.getEmail());
        validateCompany(addingEmployeeRequest.getContract().getCompanyId());
    }

    private void validateCompany(Long id) {
        if (!companyRepository.existsById(id)){
            throw new ValidationException("Company not found.");
        }
    }

    private void validateMail(String mail) {
        if(employeeRepository.existsByEmail(mail)){
            throw new ValidationException("Email is not unique.");
        }
    }

    private Employee getEmployee(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            throw new ValidationException("Employee not found.");
        }
        return employeeOptional.get();
    }

}
