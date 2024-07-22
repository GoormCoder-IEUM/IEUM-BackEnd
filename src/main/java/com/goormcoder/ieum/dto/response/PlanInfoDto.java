package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Plan;

public record PlanInfoDto(

        Long planId

) {

    public static PlanInfoDto of(Plan plan) {
        return new PlanInfoDto(plan.getId());
    }

}
