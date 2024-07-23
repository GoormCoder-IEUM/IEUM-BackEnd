package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Member;

import java.util.List;
import java.util.UUID;

public record MemberSummaryDto(
        UUID id,
        String name
) {
    public static MemberSummaryDto of(Member member) {
        return new MemberSummaryDto(
                member.getId(),
                member.getName()
        );
    }

    public static List<MemberSummaryDto> listOf(List<Member> members){
        return members
                .stream()
                .map(MemberSummaryDto::of)
                .toList();
    }
}
