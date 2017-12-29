package com.example.ist.exception;

public class ViolatedPasswordPolicyException extends RuntimeException {
    public ViolatedPasswordPolicyException(String message) {
        super(message);
    }

    public ViolatedPasswordPolicyException(String message, Throwable cause) {
        super(message, cause);
    }
}
