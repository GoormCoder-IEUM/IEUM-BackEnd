package com.goormcoder.ieum.exception;

import com.goormcoder.ieum.domain.Member;
import com.goormcoder.ieum.domain.Plan;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlaceShareWebSocketException extends BusinessException {

    private final UUID memberId;
    private final Long planId;

    public PlaceShareWebSocketException(ErrorMessages errorMessage, Member member, Plan plan) {
        super(errorMessage);
        this.memberId = member != null ? member.getId() : null;
        this.planId = plan != null ? plan.getId() : null;
    }

}
