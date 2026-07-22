package com.hzhan.Enum;

public enum Action {
    SESSION("session"),
    STAKE("stake"),
    HIGHSTAKES("highstakes");

    private final String value;

    Action(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
