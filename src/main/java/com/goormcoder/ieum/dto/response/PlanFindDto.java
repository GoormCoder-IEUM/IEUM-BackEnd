package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Plan;
import com.goormcoder.ieum.domain.enumeration.PlanVehicle;

import java.time.LocalDateTime;
import java.util.List;

public record PlanFindDto(

        String destinationKrName,
        String destinationEnName,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        PlanVehicle vehicle,
        List<PlanMemberFindDto> planMembers

) {

    public static PlanFindDto of(Plan plan) {
        return new PlanFindDto(
                plan.getDestination().getDestinationName().getKrName(),
                plan.getDestination().getDestinationName().getEnName(),
                plan.getStartedAt(),
                plan.getEndedAt(),
                plan.getVehicle(),
                plan.getPlanMembers().stream()
                        .map(PlanMemberFindDto::of)
                        .toList()
        );
    }

}
