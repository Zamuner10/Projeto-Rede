package com.example.vinicius.exception;

public class PrivateProfileAccessException extends RuntimeException {
    public PrivateProfileAccessException(String message) {
        super(message);
    }
}
