package com.statemachine.unit;

import com.statemachine.common.enums.EmployeeStatusEnum;
import com.statemachine.common.enums.StatusEventEnum;
import com.statemachine.core.dal.model.Employee;
import com.statemachine.core.service.StateMachineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
@AutoConfigureMockMvc
class StateMachineConfigurationTests {

    @Autowired
    StateMachineService<EmployeeStatusEnum, StatusEventEnum> stateMachineService;

    @Autowired
    StateMachineServiceImpl employeeStateMachineService;


    private StateMachine<EmployeeStatusEnum, StatusEventEnum> stateMachine;

    @BeforeEach
    public void setup() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        stateMachine = stateMachineService.acquireStateMachine(generatedString);
    }

    @Test
    void executeTransition_TransitionFails() {
        Employee empl = buildMockEmployee();
        boolean success = employeeStateMachineService.sendEvent(empl, stateMachine, StatusEventEnum.APPROVE);
        assertFalse(success);
    }

    @Test
    void executeTransition_ADDED_Success() throws Exception {
        StateMachineTestPlan<EmployeeStatusEnum, StatusEventEnum> plan =
                StateMachineTestPlanBuilder
                        .<EmployeeStatusEnum, StatusEventEnum>builder()
                        .stateMachine(stateMachine).step()
                        .expectState(EmployeeStatusEnum.ADDED)
                        .and().build();
        plan.test();
    }

    @Test
    void executeTransition_INCECK_Success() throws Exception {

        StateMachineTestPlan<EmployeeStatusEnum, StatusEventEnum> plan =
                StateMachineTestPlanBuilder
                        .<EmployeeStatusEnum, StatusEventEnum>builder().stateMachine(stateMachine).step()
                        .sendEvent(MessageBuilder.withPayload(StatusEventEnum.BEGIN_CHECK).build())
                        .expectStates(EmployeeStatusEnum.IN_CHECK,
                                EmployeeStatusEnum.SECURITY_CHECK_STARTED,
                                EmployeeStatusEnum.WORK_PERMIT_CHECK_STARTED)
                        .expectStateChanged(2).and().build();

        plan.test();
    }

    @Test
    void executeTransition_APPROVE_Success() throws Exception {

        StateMachineTestPlan<EmployeeStatusEnum, StatusEventEnum> plan =
                StateMachineTestPlanBuilder
                        .<EmployeeStatusEnum, StatusEventEnum>builder()
                        .stateMachine(stateMachine).step()
                        .sendEvent(MessageBuilder.withPayload(StatusEventEnum.BEGIN_CHECK).build())

                        .expectStates(EmployeeStatusEnum.IN_CHECK,
                                EmployeeStatusEnum.SECURITY_CHECK_STARTED,
                                EmployeeStatusEnum.WORK_PERMIT_CHECK_STARTED)
                        .expectStateChanged(2).and().step()

                        .sendEvent(MessageBuilder.withPayload(StatusEventEnum.FINISH_SECURITY_CHECK).build())
                        .expectStates(EmployeeStatusEnum.IN_CHECK,
                                EmployeeStatusEnum.SECURITY_CHECK_FINISHED,
                                EmployeeStatusEnum.WORK_PERMIT_CHECK_STARTED)
                        .expectStateChanged(1).and().step()

                        .sendEvent(MessageBuilder.withPayload(StatusEventEnum.COMPLETE_INITIAL_WORK_PERMIT_CHECK).build())
                        .expectStates(EmployeeStatusEnum.IN_CHECK,
                                EmployeeStatusEnum.SECURITY_CHECK_FINISHED,
                                EmployeeStatusEnum.WORK_PERMIT_CHECK_PENDING_VERIFICATION)
                        .expectStateChanged(1).and().step()

                        .sendEvent(MessageBuilder.withPayload(StatusEventEnum.FINISH_WORK_PERMIT_CHECK).build())
                        .expectStates(EmployeeStatusEnum.APPROVED)
                        .expectStateChanged(2).and()
                        .build();

        plan.test();
    }

    private Employee buildMockEmployee() {
        List<String> status = new ArrayList<>();
        status.add(EmployeeStatusEnum.ADDED.name());
        Employee employee = new Employee();
        employee.setId(1L);
        return employee;
    }
}