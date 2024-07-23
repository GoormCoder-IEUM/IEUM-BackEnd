package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.enumeration.PlanVehicle;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PlanSortDto {
    private Long id;
    private String destinationName;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private PlanVehicle vehicle;

    public static PlanSortDto of(Plan plan) {
        return PlanSortDto.builder()
                .id(plan.getId())
                .destinationName(String.valueOf(plan.getDestination()))
                .startedAt(plan.getStartedAt())
                .endedAt(plan.getEndedAt())
                .vehicle(plan.getVehicle())
                .build();
    }

    public static List<PlanSortDto> listOf(List<Plan> plans) {
        return plans.stream()
                .map(PlanSortDto::of)
                .collect(Collectors.toList());
    }
}
