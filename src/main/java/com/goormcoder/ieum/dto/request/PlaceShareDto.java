package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.constants.PlanConstants;
import jakarta.validation.constraints.NotNull;

public record PlaceShareDto(

        @NotNull(message = PlanConstants.PLAN_ID_IS_NULL)
        Long planId,

        @NotNull(message = PlanConstants.PLACE_ID_IS_NULL)
        Long placeId

) {
}
