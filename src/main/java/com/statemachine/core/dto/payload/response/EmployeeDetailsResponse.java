package com.statemachine.core.dto.payload.response;

import com.statemachine.common.enums.StatusEventEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDetailsResponse {

    @NotNull
    private Long id;

    @NotNull
    private String email;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Integer age;

    @NotNull
    private ContractDetailsResponse contract;

    @NotNull
    private List<String> currentStatusList;

    List<StatusEventEnum> expectedEventList;
}
