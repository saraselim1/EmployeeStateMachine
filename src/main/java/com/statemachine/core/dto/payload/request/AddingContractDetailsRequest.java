package com.statemachine.core.dto.payload.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddingContractDetailsRequest {

    @NotNull
    @Positive
    private Long companyId;

    @NotNull
    @Positive
    private Long duration;

    @NotNull
    @Future
    private LocalDateTime startDate;

    @NotNull
    @Positive
    private Double salary;
}
