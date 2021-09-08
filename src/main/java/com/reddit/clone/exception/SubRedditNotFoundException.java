package com.reddit.clone.exception;

public class SubRedditNotFoundException extends RuntimeException {

    public SubRedditNotFoundException(String msg) {
        super(msg);
    }
}
