package com.goormcoder.ieum.dto.request;

import java.time.LocalDateTime;

public record MemberCreateDto(
        String name,
        String gender,
        LocalDateTime birth,
        String loginId,
        String password
) {

}
