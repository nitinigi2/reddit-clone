package com.reddit.clone.exception;

import io.jsonwebtoken.security.SignatureException;

public class JwtTokenNotValidException extends RuntimeException {
    public JwtTokenNotValidException(SignatureException e) {
        super(e);
    }
}
