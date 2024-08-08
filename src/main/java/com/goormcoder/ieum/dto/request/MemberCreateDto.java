package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.domain.enumeration.Gender;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

public record MemberCreateDto(
        String name,
        Gender gender,
        LocalDate birth,
        @Email
        String email,
        String loginId,
        String password
) {

}
