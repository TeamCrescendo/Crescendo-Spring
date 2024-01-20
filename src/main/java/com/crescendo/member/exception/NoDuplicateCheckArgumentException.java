package com.crescendo.member.exception;

public class NoDuplicateCheckArgumentException extends RuntimeException{
    public NoDuplicateCheckArgumentException(String message) {
        super(message);
    }
}
