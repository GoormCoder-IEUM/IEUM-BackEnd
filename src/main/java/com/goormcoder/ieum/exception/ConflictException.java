package com.goormcoder.ieum.exception;

import lombok.Getter;

@Getter
public class ConflictException extends BusinessException {

    public ConflictException(ErrorMessages errorMessages) {
        super(errorMessages);
    }
}
