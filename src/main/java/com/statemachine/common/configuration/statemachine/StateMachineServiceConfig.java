package com.statemachine.common.configuration.statemachine;

import com.statemachine.common.enums.EmployeeStatusEnum;
import com.statemachine.common.enums.StatusEventEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@RequiredArgsConstructor
@Configuration
public class StateMachineServiceConfig {

    private final StateMachineFactory<EmployeeStatusEnum, StatusEventEnum> stateMachineFactory;
    private final StateMachineRuntimePersister<EmployeeStatusEnum, StatusEventEnum, String> stateMachineRuntimePersister;

    @Bean
    public StateMachineService<EmployeeStatusEnum, StatusEventEnum> stateMachineService() {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
    }

}