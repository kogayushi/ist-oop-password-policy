package com.example.ist.exception;

public class ViolatedPolicyException extends RuntimeException {
    public ViolatedPolicyException(String message) {
        super(message);
    }

    public ViolatedPolicyException(String message, Throwable cause) {
        super(message, cause);
    }
}
