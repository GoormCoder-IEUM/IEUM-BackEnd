package com.goormcoder.ieum.exception;

import com.goormcoder.ieum.dto.response.PlaceShareErrorDto;
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

    @MessageExceptionHandler
    @SendTo("/sub/plans/errors")
    public PlaceShareErrorDto handlePlaceShareWebSocketException(PlaceShareWebSocketException exception) {
        return PlaceShareErrorDto.of(exception);
    }

}
