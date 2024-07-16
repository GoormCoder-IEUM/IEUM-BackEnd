package com.goormcoder.ieum.dto.request;

import java.time.LocalDateTime;

public record MemberUpdateDto (
        String name,
        String gender,
        LocalDateTime birth
) {

}
