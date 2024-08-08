package com.goormcoder.ieum.dto.request;

import com.goormcoder.ieum.domain.enumeration.Gender;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;

public record MemberUpdateDto (
        String name,
        Gender gender,
        LocalDate birth,
        @Email
        String email
) {

}
