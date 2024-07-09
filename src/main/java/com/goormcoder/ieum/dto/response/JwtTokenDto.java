package com.goormcoder.ieum.dto.response;

public record JwtTokenDto(
        String accessToken,
        String refreshToken
) {

}
