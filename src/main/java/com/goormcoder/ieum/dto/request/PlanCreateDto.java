package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.constants.PlanConstants;
import com.goormcoder.ieum.domain.enumeration.PlanVehicle;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PlanCreateDto(

        @NotNull(message = PlanConstants.DESTINATION_ID_IS_NULL)
        Long destinationId,

        @NotNull(message = PlanConstants.STARTED_AT_IS_NULL)
        @FutureOrPresent(message = PlanConstants.STARTED_AT_IS_WRONG)
        LocalDateTime startedAt,

        @NotNull(message = PlanConstants.ENDED_AT_IS_NULL)
        @FutureOrPresent(message = PlanConstants.ENDED_AT_IS_WRONG)
        LocalDateTime endedAt,

        @NotNull(message = PlanConstants.VEHICLE_IS_NULL)
        PlanVehicle vehicle

) {
}
