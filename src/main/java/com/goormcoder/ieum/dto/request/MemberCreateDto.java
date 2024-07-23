package com.goormcoder.ieum.dto.request;

import java.time.LocalDate;

public record MemberCreateDto(
        String name,
        String gender,
        LocalDate birth,
        String loginId,
        String password
) {

}
