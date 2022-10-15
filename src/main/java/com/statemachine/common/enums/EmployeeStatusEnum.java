package com.statemachine.common.enums;

public enum EmployeeStatusEnum {

    ADDED,
    IN_CHECK, //Fork

    SECURITY_CHECK_STARTED,
    SECURITY_CHECK_FINISHED,

    WORK_PERMIT_CHECK_STARTED,
    WORK_PERMIT_CHECK_PENDING_VERIFICATION,
    WORK_PERMIT_CHECK_FINISHED,

    ALL_CHECKS, //Join
    APPROVED,
    ACTIVE;
}
