package com.goormcoder.ieum.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goormcoder.ieum.domain.Place;

import java.time.LocalDateTime;

public record PlaceFindDto(

        String placeName,
        String address,
        LocalDateTime startedAt,
        LocalDateTime endedAt,
        Long categoryId,
        String memberLoginId

) {

    public static PlaceFindDto of(Place place) {
        return new PlaceFindDto(
                place.getPlaceName(),
                place.getAddress(),
                place.getStartedAt(),
                place.getEndedAt(),
                place.getCategory().getId(),
                place.getMember().getLoginId());
    }

}
