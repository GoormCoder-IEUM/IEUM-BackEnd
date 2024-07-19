package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.domain.enumeration.PlanVehicle;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PlanCreateDto(

        @NotNull
        Long destinationId,

        @NotNull
        LocalDateTime startedAt,

        @NotNull
        LocalDateTime endedAt,

        @NotNull
        PlanVehicle vehicle

) {
}
