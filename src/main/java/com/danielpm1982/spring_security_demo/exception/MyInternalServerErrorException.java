package com.danielpm1982.spring_security_demo.exception;

public class MyInternalServerErrorException extends RuntimeException {
    public MyInternalServerErrorException(String message) {
        super(message);
    }
}
