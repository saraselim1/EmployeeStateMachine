package com.statemachine.util;

import com.statemachine.core.dto.payload.request.AddingContractDetailsRequest;
import com.statemachine.core.dto.payload.request.AddingEmployeeRequest;

import java.time.LocalDateTime;

public class RequestBuilder {

    public static AddingEmployeeRequest buildAddingEmployeeRequest() {
        return AddingEmployeeRequest.builder()
                .name("Name")
                .email("string@str.com")
                .age(30)
                .contract(AddingContractDetailsRequest.builder()
                        .companyId(1L)
                        .duration(1L)
                        .salary(1000D)
                        .startDate(LocalDateTime.MAX)
                        .build())
                .build();
    }
}
