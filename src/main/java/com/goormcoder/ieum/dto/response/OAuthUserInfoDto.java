package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.enumeration.Gender;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record OAuthUserInfoDto(
        String registrationId,
        String id,
        String name,
        String email,
        LocalDate birth,
        Gender gender
) {

}
