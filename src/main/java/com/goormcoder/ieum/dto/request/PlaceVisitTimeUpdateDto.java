package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.constants.PlanConstants;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PlaceVisitTimeUpdateDto(

        @NotNull(message = PlanConstants.STARTED_AT_IS_NULL)
        @FutureOrPresent(message = PlanConstants.STARTED_AT_IS_WRONG)
        LocalDateTime startedAt,

        @NotNull(message = PlanConstants.ENDED_AT_IS_NULL)
        @FutureOrPresent(message = PlanConstants.ENDED_AT_IS_WRONG)
        LocalDateTime endedAt

) {
}
