package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.domain.PlanVehicle;

import java.time.LocalDateTime;

public record PlanCreateDto(

        String location,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        PlanVehicle vehicle

) {
}
