package com.payment.gateaway.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SendOption {

    TRUE("true"),
    FALSE("false");

    private final String value;

    SendOption(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
