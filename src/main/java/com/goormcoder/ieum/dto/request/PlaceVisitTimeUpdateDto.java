package com.goormcoder.ieum.dto.request;

import java.time.LocalDateTime;

public record PlaceVisitTimeUpdateDto(

        LocalDateTime startedAt,
        LocalDateTime endedAt

) {
}
