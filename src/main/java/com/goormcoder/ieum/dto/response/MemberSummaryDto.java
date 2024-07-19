package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Invite;
import com.goormcoder.ieum.domain.Member;

import java.util.List;

public record MemberSummaryDto(
        String loginId,
        String name
) {
    public static MemberSummaryDto of(Member member) {
        return new MemberSummaryDto(
                member.getLoginId(),
                member.getName()
        );
    }

    public static List<MemberSummaryDto> listOf(List<Member> members){
        return members
                .stream()
                .map(member -> new MemberSummaryDto(
                        member.getLoginId(),
                        member.getName()
                )).toList();
    }
}
