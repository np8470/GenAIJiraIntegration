package com.genai.ollamarestapi.exception;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.validation.BindException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(
        basePackages = "com.genai.ollamarestapi.controller")
public class ApiExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(ApiExceptionHandler.class);

    private ErrorResponse buildError(

            HttpStatus status,

            String error,

            String message,

            HttpServletRequest request) {

        return new ErrorResponse(

                LocalDateTime.now(),

                status.value(),

                error,

                message,

                request.getRequestURI());

    }

    /**
     * AI Exception
     */
    @ExceptionHandler(AIException.class)
    public ResponseEntity<ErrorResponse> handleAI(

            AIException ex,

            HttpServletRequest request) {

        log.error("AI Exception", ex);

        return ResponseEntity

                .status(HttpStatus.SERVICE_UNAVAILABLE)

                .body(buildError(

                        HttpStatus.SERVICE_UNAVAILABLE,

                        "AI Service Error",

                        ex.getMessage(),

                        request));

    }

    /**
     * Jira Exception
     */
    @ExceptionHandler(JiraException.class)
    public ResponseEntity<ErrorResponse> handleJira(

            JiraException ex,

            HttpServletRequest request) {

        log.error("Jira Exception", ex);

        return ResponseEntity

                .status(HttpStatus.BAD_GATEWAY)

                .body(buildError(

                        HttpStatus.BAD_GATEWAY,

                        "Jira Error",

                        ex.getMessage(),

                        request));

    }

    /**
     * Access Denied
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDenied(

            AccessDeniedException ex,

            HttpServletRequest request) {

        return ResponseEntity

                .status(HttpStatus.FORBIDDEN)

                .body(buildError(

                        HttpStatus.FORBIDDEN,

                        "Access Denied",

                        ex.getMessage(),

                        request));

    }

    /**
     * Validation
     */
    @ExceptionHandler({

            MethodArgumentNotValidException.class,

            BindException.class

    })
    public ResponseEntity<ErrorResponse> validation(

            Exception ex,

            HttpServletRequest request) {

        return ResponseEntity

                .badRequest()

                .body(buildError(

                        HttpStatus.BAD_REQUEST,

                        "Validation Error",

                        ex.getMessage(),

                        request));

    }

    /**
     * Catch All
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generic(

            Exception ex,

            HttpServletRequest request) {

        log.error("Unexpected Error", ex);

        return ResponseEntity

                .status(HttpStatus.INTERNAL_SERVER_ERROR)

                .body(buildError(

                        HttpStatus.INTERNAL_SERVER_ERROR,

                        "Internal Server Error",

                        ex.getMessage(),

                        request));

    }

}