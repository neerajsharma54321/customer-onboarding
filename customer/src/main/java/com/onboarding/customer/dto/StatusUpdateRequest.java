package com.onboarding.customer.dto;

import com.onboarding.customer.dto.enums.AppStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StatusUpdateRequest {
    @NotNull
    private AppStatus status;
}
