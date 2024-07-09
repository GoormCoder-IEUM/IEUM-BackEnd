package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.MemberRole;
import java.time.LocalDateTime;
import java.util.UUID;

public record MemberFindDto(
        UUID id,

        String name,
        MemberRole role,
        String gender,
        LocalDateTime birth,

        String loginId,
        String oauthType,

        LocalDateTime createdAt,
        LocalDateTime deletedAt
) {

    public static MemberFindDto of(Member member) {
        return new MemberFindDto(
                member.getId(),

                member.getName(),
                member.getRole(),
                member.getGender(),
                member.getBirth(),

                member.getLoginId(),
                member.getOauthType(),

                member.getCreatedAt(),
                member.getDeletedAt()
        );
    }

}
