package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Place;

public record PlaceFindDto(

        Long planId,
        String placeName,
        String address,
        Long categoryId,
        String memberLoginId

) {

    public static PlaceFindDto of(Place place) {
        return new PlaceFindDto(
                place.getPlan().getId(),
                place.getPlaceName(),
                place.getAddress(),
                place.getCategory().getId(),
                place.getMember().getLoginId()
                );
    }

}
