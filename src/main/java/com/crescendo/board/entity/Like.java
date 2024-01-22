package com.crescendo.board.entity;

public enum Like {

    LIKE(0), UNLIKE(1);

    private final int value;

    Like(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
