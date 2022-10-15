package com.statemachine.core.service;

import com.statemachine.common.constant.EmployeeConstant;
import com.statemachine.common.enums.EmployeeStatusEnum;
import com.statemachine.common.enums.StatusEventEnum;
import com.statemachine.core.dal.model.Employee;
import com.statemachine.core.interceptor.EmployeeStatePersistChangeInterceptor;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;


@Log
@Service
@AllArgsConstructor
public class StateMachineServiceImpl implements StateMachineService {

    private final org.springframework.statemachine.service.StateMachineService<EmployeeStatusEnum, StatusEventEnum> stateMachineService;
    private final EmployeeStatePersistChangeInterceptor employeeStatePersistChangeInterceptor;


    public StateMachine<EmployeeStatusEnum, StatusEventEnum> buildStateMachine(Employee employee) {

        StateMachine<EmployeeStatusEnum, StatusEventEnum> sm =
                stateMachineService.acquireStateMachine(Long.toString(employee.getId()));
        sm.stopReactively();
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> sma.addStateMachineInterceptor(employeeStatePersistChangeInterceptor));
        sm.startReactively();
        return sm;
    }

    public boolean sendEvent(Employee employee, StateMachine<EmployeeStatusEnum, StatusEventEnum> sm,
                              StatusEventEnum event){

        Message<StatusEventEnum> msg = MessageBuilder.withPayload(event)
                .setHeader(EmployeeConstant.EMPLOYEE_HEADER, employee)
                .build();

        return sm.sendEvent(msg);
    }
}
