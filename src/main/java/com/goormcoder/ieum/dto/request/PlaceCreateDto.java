package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.constants.PlanConstants;
import jakarta.validation.constraints.NotNull;

public record PlaceCreateDto(

        @NotNull(message = PlanConstants.PLACE_INFO_IS_NULL)
        String placeName,

        @NotNull(message = PlanConstants.PLACE_INFO_IS_NULL)
        String address,

        @NotNull(message = PlanConstants.CATEGORY_ID_IS_NULL)
        Long categoryId

) {
}
