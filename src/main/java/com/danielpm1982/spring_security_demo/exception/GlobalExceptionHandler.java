package com.danielpm1982.spring_security_demo.exception;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.logging.Level;

@Log
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MyInternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ErrorResponse handleMyException(MyInternalServerErrorException e){
        log.log(Level.WARNING, e::toString);
        return new ErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE.name(), e.getMessage());
    }
    @ExceptionHandler(MyNotFoundErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorResponse handleMyNotFoundErrorException(MyNotFoundErrorException e){
        log.log(Level.WARNING, e::toString);
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ErrorResponse handleException(Exception e){
        log.log(Level.WARNING, e::toString);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage());
    }
}
