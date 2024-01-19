package com.crescendo.member.exception;

public class NoEmailArgumentException extends RuntimeException{
    public NoEmailArgumentException(String message) {
        super(message);
    }
}
