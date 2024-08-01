package com.goormcoder.ieum.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goormcoder.ieum.exception.PlaceShareWebSocketException;

import java.util.UUID;

public record PlaceShareErrorDto(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        UUID memberId,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long planId,

        String errorMessage

) {

    public static PlaceShareErrorDto of(PlaceShareWebSocketException exception) {
        return new PlaceShareErrorDto(
                exception.getMemberId(),
                exception.getPlanId(),
                exception.getMessage());
    }

}
