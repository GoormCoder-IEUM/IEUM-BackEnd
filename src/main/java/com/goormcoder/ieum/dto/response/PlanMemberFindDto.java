package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.PlanMember;

import java.util.UUID;

public record PlanMemberFindDto(

        UUID memberId

) {

    public static PlanMemberFindDto of(PlanMember planMember) {
        return new PlanMemberFindDto(planMember.getMember().getId());
    }

}
