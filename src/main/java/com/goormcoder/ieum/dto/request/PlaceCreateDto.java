package com.goormcoder.ieum.dto.request;

public record PlaceCreateDto(

        String placeName,
        String address,
        Long categoryId

) {
}
