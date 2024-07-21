package com.goormcoder.ieum.dto.request;

public record PlaceCreateDto(

        Long planId,
        String placeName,
        String address,
        Long categoryId

) {
}
