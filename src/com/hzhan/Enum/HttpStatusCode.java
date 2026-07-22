package com.hzhan.Enum;

public enum HttpStatusCode {
    OK(200),
    NOT_ALLOWED(401),
    NOT_FOUND(404),
    SERVER_ERROR(500);

    private int value;

    HttpStatusCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
