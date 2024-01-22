package com.crescendo.board.entity;

public enum Like {

    LIKE(true), UNLIKE(false);

    private final boolean value;

    Like(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}
