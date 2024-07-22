package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Place;

public record PlaceInfoDto(

        Long placeId

) {

    public static PlaceInfoDto of(Place place) {
        return new PlaceInfoDto(place.getId());
    }

}
