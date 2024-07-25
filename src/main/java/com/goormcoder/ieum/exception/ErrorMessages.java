package com.goormcoder.ieum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    // 400 BAD_REQUEST
    BAD_REQUEST_PLACE_VISIT_TIME("방문일시는 일정 시작일시 이전이거나 종료일시 이후일 수 없습니다."),
    BAD_REQUEST_PLACE_NOT_ACTIVE("방문일시는 장소 공유 후 설정 가능합니다."),
    BAD_REQUEST_DAY_NOT_IN_DURATION("일정기간을 벗어납니다."),

    // 403 FORBIDDEN
    FORBIDDEN_ACCESS("해당 요청에 대한 접근 권한이 없습니다."),

    // 404 NOT_FOUND
    MEMBER_NOT_FOUND("해당하는 일정이 존재하지 않습니다."),
    DESTINATION_NOT_FOUND("해당하는 여행지가 존재하지 않습니다."),
    INVITE_NOT_FOUND("존재하지 않는 초대입니다."),
    CATEGORY_NOT_FOUND("해당하는 카테고리가 존재하지 않습니다."),
    PLACE_NOT_FOUND("해당하는 장소가 존재하지 않습니다."),
    PLAN_NOT_FOUND("해당하는 일정이 존재하지 않습니다."),
    PLAN_MEMBER_NOT_FOUND("해당하는 사용자는 해당 일정 멤버가 아닙니다."),

    // 409 CONFLICT
    INVITE_REQUEST_CONFLICT("이미 초대한 멤버입니다."),
    INVITE_RESPONSE_CONFLICT("이미 응답 또는 취소된 초대입니다."),
    PLACE_CONFLICT("해당 장소는 이미 추가되었습니다.")

    ;

    private final String message;

}
