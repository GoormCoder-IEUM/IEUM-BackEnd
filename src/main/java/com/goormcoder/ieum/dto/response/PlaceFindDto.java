package com.goormcoder.ieum.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.goormcoder.ieum.domain.Place;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PlaceFindDto(

        Long id,
        String placeName,
        String address,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime startedAt,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime endedAt,
        
        Long categoryId,
        UUID memberId

) {

    public static PlaceFindDto of(Place place) {
        return new PlaceFindDto(
                place.getId(),
                place.getPlaceName(),
                place.getAddress(),
                place.getStartedAt(),
                place.getEndedAt(),
                place.getCategory().getId(),
                place.getMember().getId());
    }

    public static List<PlaceFindDto> listOf(List<Place> places) {
        return places.stream()
                .map(PlaceFindDto::of).toList();
    }

}
