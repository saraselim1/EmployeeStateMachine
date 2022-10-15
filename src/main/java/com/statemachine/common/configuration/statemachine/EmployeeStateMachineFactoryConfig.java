package com.statemachine.common.configuration.statemachine;

import com.statemachine.common.enums.EmployeeStatusEnum;
import com.statemachine.common.enums.StatusEventEnum;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;

@EnableStateMachineFactory
@Configuration
@AllArgsConstructor
public class EmployeeStateMachineFactoryConfig extends
        StateMachineConfigurerAdapter<EmployeeStatusEnum, StatusEventEnum> {

    private final JpaPersistingStateMachineInterceptor<EmployeeStatusEnum, StatusEventEnum, String> persister;

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeStatusEnum, StatusEventEnum> config)
            throws Exception {
        config
                .withPersistence()
                .runtimePersister(persister);
        config
                .withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeStatusEnum, StatusEventEnum> states) throws Exception {
        states
                .withStates()
                .initial(EmployeeStatusEnum.ADDED)
                .end(EmployeeStatusEnum.ACTIVE)
                .state(EmployeeStatusEnum.APPROVED)

                .fork(EmployeeStatusEnum.IN_CHECK)
                .join(EmployeeStatusEnum.ALL_CHECKS)

                .and().withStates()
                .parent(EmployeeStatusEnum.IN_CHECK)
                .initial(EmployeeStatusEnum.SECURITY_CHECK_STARTED)
                .end(EmployeeStatusEnum.SECURITY_CHECK_FINISHED)

                .and().withStates()
                .parent(EmployeeStatusEnum.IN_CHECK)
                .initial(EmployeeStatusEnum.WORK_PERMIT_CHECK_STARTED)
                .state(EmployeeStatusEnum.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
                .end(EmployeeStatusEnum.WORK_PERMIT_CHECK_FINISHED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeStatusEnum, StatusEventEnum> transitions) throws Exception {


        transitions.withExternal()
                .source(EmployeeStatusEnum.ADDED)
                .target(EmployeeStatusEnum.IN_CHECK)
                .event(StatusEventEnum.BEGIN_CHECK)

                .and().withExternal()
                .source(EmployeeStatusEnum.SECURITY_CHECK_STARTED)
                .target(EmployeeStatusEnum.SECURITY_CHECK_FINISHED)
                .event(StatusEventEnum.FINISH_SECURITY_CHECK)

                .and().withExternal()
                .source(EmployeeStatusEnum.WORK_PERMIT_CHECK_STARTED)
                .target(EmployeeStatusEnum.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
                .event(StatusEventEnum.COMPLETE_INITIAL_WORK_PERMIT_CHECK)

                .and().withExternal()
                .source(EmployeeStatusEnum.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
                .target(EmployeeStatusEnum.WORK_PERMIT_CHECK_FINISHED)
                .event(StatusEventEnum.FINISH_WORK_PERMIT_CHECK)

                .and().withExternal()
                .source(EmployeeStatusEnum.ALL_CHECKS)
                .target(EmployeeStatusEnum.APPROVED)

                .and().withExternal()
                .source(EmployeeStatusEnum.APPROVED)
                .target(EmployeeStatusEnum.ACTIVE)
                .event(StatusEventEnum.ACTIVATE)

                .and().withFork()
                .source(EmployeeStatusEnum.IN_CHECK)
                .target(EmployeeStatusEnum.SECURITY_CHECK_STARTED)
                .target(EmployeeStatusEnum.WORK_PERMIT_CHECK_STARTED)

                .and().withJoin()
                .source((EmployeeStatusEnum.SECURITY_CHECK_FINISHED))
                .source(EmployeeStatusEnum.WORK_PERMIT_CHECK_FINISHED)
                .target(EmployeeStatusEnum.ALL_CHECKS);
    }

}
