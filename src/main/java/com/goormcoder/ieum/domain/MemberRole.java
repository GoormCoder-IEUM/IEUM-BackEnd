package com.goormcoder.ieum.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberRole {

    ADMIN("admin"),
    USER("user"),
    ;

    private final String value;

}
