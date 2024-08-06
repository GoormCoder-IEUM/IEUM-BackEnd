package com.goormcoder.ieum.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
@Getter
public enum MemberRole implements GrantedAuthority {

    ADMIN("admin"),
    USER("user"),
    ;

    private final String value;

    @Override
    public String getAuthority() {
        return value;
    }
}
