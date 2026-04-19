package com.example.library.exception;

public class OperationConflictException extends RuntimeException {
    public OperationConflictException(String message) {
        super(message);
    }
}
