package com.bing.exceptions;

public class VerifyCodeException extends RuntimeException {
    public VerifyCodeException(String message) {
        super(message);
    }
}
