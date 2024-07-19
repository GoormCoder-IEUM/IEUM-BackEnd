package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Member;

import java.util.List;

public record MemberFindAllDto(
        String loginId,
        String name
) {

    public static List<MemberFindAllDto> listOf(List<Member> members) {
        return members
                .stream()
                .map(member -> new MemberFindAllDto(
                        member.getLoginId(),
                        member.getName()
                )).toList();
    }

}
