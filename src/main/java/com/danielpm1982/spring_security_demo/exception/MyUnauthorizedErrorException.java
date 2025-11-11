package com.danielpm1982.spring_security_demo.exception;

public class MyUnauthorizedErrorException extends RuntimeException {
    public MyUnauthorizedErrorException(String message) {
        super(message);
    }
}
