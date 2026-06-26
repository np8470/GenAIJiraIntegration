package com.genai.ollamarestapi.exception;

public class JiraException extends RuntimeException {

    public JiraException(
            String message,
            Throwable cause) {

        super(message, cause);
    }

    public JiraException(String message) {

        super(message);
    }
}