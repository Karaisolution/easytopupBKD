package com.payment.gateaway.exception;

import com.fasterxml.jackson.databind.JsonNode;

public class PaymentException extends RuntimeException {
    private final int statusCode;
    private final JsonNode errorBody;

    public PaymentException(String message, int statusCode, JsonNode errorBody) {
        super(message);
        this.statusCode = statusCode;
        this.errorBody = errorBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public JsonNode getErrorBody() {
        return errorBody;
    }
}

