package com.goormcoder.ieum.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DestinationType {

    JEJU("제주", "JEJU"),
    BUSAN("부산", "BUSAN"),
    SEOUL("서울", "SEOUL"),
    GYEONGJU("경주", "GYEONGJU"),
    GANGNEUNG("강릉", "GANGNEUNG"),
    YEOSU("여수", "YEOSU"),
    JEONJU("전주", "JEONJU"),
    POHANG("포항", "POHANG"),
    INCHEON("인천", "INCHEON"),
    DAEJEON("대전", "DAEJEON"),
    ;

    private final String krName;
    private final String enName;

}
