package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.domain.PlanVehicle;

import java.time.LocalDateTime;

public record PlanCreateDto(

        Long destinationId,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        PlanVehicle vehicle

) {
}
