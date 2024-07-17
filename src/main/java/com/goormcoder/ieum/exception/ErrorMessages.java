package com.goormcoder.ieum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    // 404 NOT_FOUND
    MEMBER_NOT_FOUND("해당하는 일정이 존재하지 않습니다."),

    ;

    private final String message;

}
