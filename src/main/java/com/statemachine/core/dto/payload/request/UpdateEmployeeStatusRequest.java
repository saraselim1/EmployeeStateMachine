package com.statemachine.core.dto.payload.request;

import com.statemachine.common.enums.StatusEventEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEmployeeStatusRequest {

    @NotNull
    private StatusEventEnum event;
}
