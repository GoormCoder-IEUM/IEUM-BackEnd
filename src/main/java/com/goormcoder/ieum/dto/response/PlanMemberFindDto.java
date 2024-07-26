package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.PlanMember;

public record PlanMemberFindDto(

        String memberLoginId

) {

    public static PlanMemberFindDto of(PlanMember planMember) {
        return new PlanMemberFindDto(planMember.getMember().getLoginId());
    }

}
