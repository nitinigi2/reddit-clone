package com.reddit.clone.exception;

public class JwtTokenNotValidException extends RuntimeException {

    public JwtTokenNotValidException(String e) {
        super(e);
    }
}
