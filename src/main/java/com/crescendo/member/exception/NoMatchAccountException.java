package com.crescendo.member.exception;

public class NoMatchAccountException extends RuntimeException{
    public NoMatchAccountException(String message) {
        super(message);
    }
}
