package com.example.tgbotrusweb.exception;

public class LLMRequestsLimitException extends RuntimeException {
    public LLMRequestsLimitException(String message) {
        super(message);
    }
}
