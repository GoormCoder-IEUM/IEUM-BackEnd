package com.goormcoder.ieum.dto.response;

import com.goormcoder.ieum.domain.Invite;
import com.goormcoder.ieum.domain.enumeration.DestinationName;

import java.util.List;

public record InviteFindAllDto(
        Long planId,
        String planName, //TODO : 일정 이름 추후 추가
        DestinationName destinationName,
        MemberSummaryDto member
) {

    public static List<InviteFindAllDto> listOf(List<Invite> invites) {
        return invites
                .stream()
                .filter(invite -> invite.getDeletedAt() == null && invite.getAcceptance() == null)
                .map(invite -> new InviteFindAllDto(
                        invite.getPlan().getId(),
                        "임시 이름",
                        invite.getPlan().getDestination().getDestinationName(),
                        MemberSummaryDto.of(invite.getMember())
                )).toList();
    }

}
