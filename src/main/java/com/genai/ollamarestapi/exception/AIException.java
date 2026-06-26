package com.genai.ollamarestapi.exception;

public class AIException extends RuntimeException {

    public AIException(
            String message,
            Throwable cause) {

        super(message, cause);
    }

    public AIException(String message) {

        super(message);
    }
}