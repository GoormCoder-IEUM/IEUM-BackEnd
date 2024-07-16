package com.goormcoder.ieum.dto.request;

public record PasswordUpdateDto(
        String previousPassword,
        String newPassword
) {

}
