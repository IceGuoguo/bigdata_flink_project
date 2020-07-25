package com.bing.exceptions;

public class UserNameAndPasswordException extends RuntimeException {
    public UserNameAndPasswordException(String message) {
        super(message);
    }
}
