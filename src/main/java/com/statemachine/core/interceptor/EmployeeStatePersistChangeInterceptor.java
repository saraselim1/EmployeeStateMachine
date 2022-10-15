package com.statemachine.core.interceptor;


import com.statemachine.common.constant.EmployeeConstant;
import com.statemachine.common.enums.EmployeeStatusEnum;
import com.statemachine.common.enums.StatusEventEnum;
import com.statemachine.core.dal.model.Employee;
import com.statemachine.core.dal.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class EmployeeStatePersistChangeInterceptor extends StateMachineInterceptorAdapter<EmployeeStatusEnum, StatusEventEnum> {

    private final EmployeeRepository employeeRepository;

    @Override
    public void preStateChange(State<EmployeeStatusEnum, StatusEventEnum> state,
                               Message<StatusEventEnum> message,
                               Transition<EmployeeStatusEnum, StatusEventEnum> transition,
                               StateMachine<EmployeeStatusEnum, StatusEventEnum> stateMachine,
                               StateMachine<EmployeeStatusEnum, StatusEventEnum> rootStateMachine) {

        Optional.ofNullable(message).ifPresent(msg ->
                Optional.ofNullable(Employee.class.cast(msg.getHeaders().getOrDefault(EmployeeConstant.EMPLOYEE_HEADER, null))).ifPresent(employee -> {
                    employee.setCurrentStatusList(
                            rootStateMachine.getState().getIds().stream().map(EmployeeStatusEnum::name).collect(Collectors.toList()));
                    employeeRepository.save(employee);
                })
        );
    }

    @Override
    public Exception stateMachineError(StateMachine<EmployeeStatusEnum, StatusEventEnum> stateMachine, Exception exception) {
        log.error("{} StateMachine throws exception: [ message: {}]", "stateMachineError()", exception.getMessage());
        return super.stateMachineError(stateMachine, exception);
    }
}