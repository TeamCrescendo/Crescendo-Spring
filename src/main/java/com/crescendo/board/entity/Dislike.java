package com.crescendo.board.entity;

public enum Dislike {

    DISLIKE(1), UNDISLIKE(0);

    private final int value;

    Dislike(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}


