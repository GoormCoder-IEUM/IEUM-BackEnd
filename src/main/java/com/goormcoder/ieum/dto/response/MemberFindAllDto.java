package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Member;

import java.util.List;
import java.util.UUID;

public record MemberFindAllDto(
        UUID id,
        String name
) {

    public static List<MemberFindAllDto> listOf(List<Member> members) {
        return members
                .stream()
                .map(member -> new MemberFindAllDto(
                        member.getId(),
                        member.getName()
                )).toList();
    }

}
