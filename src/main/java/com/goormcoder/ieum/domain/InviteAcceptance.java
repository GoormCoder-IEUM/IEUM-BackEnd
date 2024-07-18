package com.goormcoder.ieum.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InviteAcceptance {

    ACCEPT("accept"),
    REFUSE("refuse"),
    ;

    private final String value;

}
