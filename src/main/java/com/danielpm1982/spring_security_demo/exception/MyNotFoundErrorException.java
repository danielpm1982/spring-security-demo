package com.danielpm1982.spring_security_demo.exception;

public class MyNotFoundErrorException extends RuntimeException {
    public MyNotFoundErrorException(String message) {
        super(message);
    }
}
