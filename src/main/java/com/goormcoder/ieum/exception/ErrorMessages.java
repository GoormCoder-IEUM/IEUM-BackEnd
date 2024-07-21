package com.goormcoder.ieum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    // 404 NOT_FOUND
    MEMBER_NOT_FOUND("해당하는 일정이 존재하지 않습니다."),
    DESTINATION_NOT_FOUND("해당하는 여행지가 존재하지 않습니다."),
    INVITE_NOT_FOUND("존재하지 않는 초대입니다."),
    CATEGORY_NOT_FOUND("해당하는 카테고리가 존재하지 않습니다."),
    PLACE_NOT_FOUND("해당하는 장소가 존재하지 않습니다."),

    // 409 CONFLICT
    INVITE_REQUEST_CONFLICT("이미 초대한 멤버입니다."),
    INVITE_RESPONSE_CONFLICT("이미 응답 또는 취소된 초대입니다.")

    ;

    private final String message;

}
