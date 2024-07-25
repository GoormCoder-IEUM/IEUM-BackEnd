package com.goormcoder.ieum.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goormcoder.ieum.domain.Place;

import java.time.LocalDateTime;
import java.util.List;

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

    public static List<PlaceFindDto> listOf(List<Place> places) {
        return places.stream()
                .map(PlaceFindDto::of).toList();
    }

}
