package com.statemachine.core.service;

import com.statemachine.common.enums.EmployeeStatusEnum;
import com.statemachine.common.enums.StatusEventEnum;
import com.statemachine.core.dal.model.Employee;
import org.springframework.statemachine.StateMachine;

public interface StateMachineService {

    StateMachine<EmployeeStatusEnum, StatusEventEnum> buildStateMachine(Employee employee);
    boolean sendEvent(Employee employee, StateMachine<EmployeeStatusEnum, StatusEventEnum> sm,
                      StatusEventEnum event);
}
