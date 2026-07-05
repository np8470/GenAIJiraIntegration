package com.genai.ollamarestapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.ui.Model;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(
        basePackages = "com.genai.ollamarestapi.controller")
public class WebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

    @ExceptionHandler(AIException.class)
    public String ai(

            AIException ex,

            Model model) {

        log.error("AI Exception", ex);

        model.addAttribute(
                "title",
                "AI Service Error");

        model.addAttribute(
                "message",
                ex.getMessage());

        return "error";

    }

    @ExceptionHandler(JiraException.class)
    public String jira(

            JiraException ex,

            Model model) {

        log.error("Jira Exception", ex);

        model.addAttribute(
                "title",
                "Jira Error");

        model.addAttribute(
                "message",
                ex.getMessage());

        return "error";

    }

    @ExceptionHandler(AccessDeniedException.class)
    public String accessDenied() {

        return "access-denied";

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String validation(

            MethodArgumentNotValidException ex,

            Model model) {

        model.addAttribute(
                "title",
                "Validation Error");

        model.addAttribute(
                "message",
                ex.getMessage());

        return "error";

    }

    /* @ExceptionHandler({
            AIException.class,
            JiraException.class
    })
    public String generic(

            Exception ex,

            Model model) {

        log.error("Unexpected Error", ex);

        model.addAttribute(
                "title",
                "Unexpected Error");

        model.addAttribute(
                "message",
                ex.getMessage());

        return "error";

    } */

}