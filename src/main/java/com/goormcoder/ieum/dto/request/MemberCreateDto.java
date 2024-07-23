package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.domain.enumeration.Gender;
import java.time.LocalDate;

public record MemberCreateDto(
        String name,
        Gender gender,
        LocalDate birth,
        String loginId,
        String password
) {

}
