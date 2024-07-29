package com.goormcoder.ieum.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebSocketExceptionHandler {

    @MessageExceptionHandler
    @SendTo("/sub/errors")
    public String handleException(Exception exception) {
        return exception.getMessage();
    }

}
