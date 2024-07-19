package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Member;

import java.util.List;

public record InviteResultDto(
        List<MemberSummaryDto> successMembers,
        List<MemberSummaryDto> duplicateMembers
) {

    public static InviteResultDto of(List<Member> successMembers, List<Member> duplicateMembers) {
        return new InviteResultDto(
                MemberSummaryDto.listOf(successMembers),
                MemberSummaryDto.listOf(duplicateMembers)
        );
    }

}
