package com.goormcoder.ieum.dto.request;

import java.time.LocalDate;

public record MemberUpdateDto (
        String name,
        String gender,
        LocalDate birth
) {

}
