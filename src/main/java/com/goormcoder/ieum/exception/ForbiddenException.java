package com.goormcoder.ieum.exception;

public class ForbiddenException extends BusinessException {

    public ForbiddenException(ErrorMessages errorMessages) {
        super(errorMessages);
    }

}
