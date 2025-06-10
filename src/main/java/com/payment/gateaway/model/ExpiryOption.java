package com.payment.gateaway.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExpiryOption {

    FIVE_MINUTES("5 mins"),
    TEN_MINUTES("10 mins"),
    ONE_HOUR("1 hour");

    private final String value;

    ExpiryOption(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
