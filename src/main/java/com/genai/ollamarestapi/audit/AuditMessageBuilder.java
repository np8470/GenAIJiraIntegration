package com.genai.ollamarestapi.audit;

import java.security.Principal;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuditMessageBuilder {

    public String buildMessage(
            Audit audit,
            Object[] args,
            Object result) {

        String message = audit.message();

        // =============================
        // Replace {0}, {1}, {2}...
        // =============================

        if (args != null) {

            for (int i = 0; i < args.length; i++) {

                Object arg = args[i];

                if (arg == null) {
                    continue;
                }

                // Ignore framework objects
                if (arg instanceof HttpSession
                        || arg instanceof HttpServletRequest
                        || arg instanceof HttpServletResponse
                        || arg instanceof Principal) {

                    continue;
                }

                message = message.replace(
                        "{" + i + "}",
                        String.valueOf(arg));
            }
        }

        // =============================
        // Append arguments (optional)
        // =============================

        if (audit.includeArguments() && args != null && args.length > 0) {

            String parameters = Arrays.stream(args)

                    .filter(arg ->
                            !(arg instanceof HttpSession)
                                    && !(arg instanceof HttpServletRequest)
                                    && !(arg instanceof HttpServletResponse)
                                    && !(arg instanceof Principal))

                    .map(arg -> arg == null ? "null" : arg.toString())

                    .collect(Collectors.joining(", "));

            message += " | Arguments: " + parameters;
        }

        // =============================
        // Append result (optional)
        // =============================

        if (audit.includeResult() && result != null) {

            message += " | Result: " + result;
        }

        return message;
    }

}