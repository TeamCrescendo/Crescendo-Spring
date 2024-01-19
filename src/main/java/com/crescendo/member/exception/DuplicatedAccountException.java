package com.crescendo.member.exception;

public class DuplicatedAccountException extends RuntimeException{
    public DuplicatedAccountException(String message) {
        super(message);
    }
}
