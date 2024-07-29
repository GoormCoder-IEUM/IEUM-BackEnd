package com.goormcoder.ieum.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChatSendDto(
        @NotNull
        Long planId,

        @NotNull
        String message
) {
}
