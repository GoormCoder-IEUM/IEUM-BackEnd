package com.goormcoder.ieum.exception;

public class BusinessException extends RuntimeException {
    private ErrorMessages errorMessages;
    public BusinessException(ErrorMessages errorMessages) {
        super(errorMessages.getMessage());
        this.errorMessages = errorMessages;
    }
}
