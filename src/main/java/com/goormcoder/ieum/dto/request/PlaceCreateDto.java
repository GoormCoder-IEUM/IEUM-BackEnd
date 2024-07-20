package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.domain.Category;
import com.goormcoder.ieum.domain.enumeration.CategoryType;

import java.time.LocalDateTime;

public record PlaceCreateDto(

        Long planId,
        String placeName,
        String address,
        Long categoryId

) {
}
