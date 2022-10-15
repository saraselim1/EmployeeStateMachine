package com.statemachine.core.dto.payload.request;


import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddingEmployeeRequest {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @Min(value = 18)
    private Integer age;

    @NotNull
    private AddingContractDetailsRequest contract;

}
