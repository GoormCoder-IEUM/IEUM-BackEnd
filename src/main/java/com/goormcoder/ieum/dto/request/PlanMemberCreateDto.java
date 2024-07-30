package com.goormcoder.ieum.dto.request;

import java.util.UUID;

public record PlanMemberCreateDto(

        UUID[] memberIds

) {
}
