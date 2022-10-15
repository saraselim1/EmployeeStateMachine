package com.statemachine.common.configuration.statemachine;


import com.statemachine.common.enums.EmployeeStatusEnum;
import com.statemachine.common.enums.StatusEventEnum;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
@EntityScan({"org.springframework.statemachine.data.jpa", "com.statemachine.core.dal"})
public class StateMachinePersistenceConfiguration {

    @Bean
    public JpaPersistingStateMachineInterceptor<EmployeeStatusEnum, StatusEventEnum, String>
    jpaPersistingStateMachineInterceptor(final JpaStateMachineRepository stateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(stateMachineRepository);
    }
    
    @Bean
    public StateMachineRuntimePersister<EmployeeStatusEnum, StatusEventEnum, String> stateMachineRuntimePersister(
            final JpaStateMachineRepository jpaStateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }
}
