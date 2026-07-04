package com.genai.ollamarestapi.audit;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    private final AuditService auditService;
    private final HttpServletRequest request;
    private final AuditMessageBuilder messageBuilder;

    public AuditAspect(AuditService auditService,
                       HttpServletRequest request,
                       AuditMessageBuilder messageBuilder) {

        this.auditService = auditService;
        this.request = request;
        this.messageBuilder = messageBuilder;
    }

    @Around("@annotation(audit)")
    public Object logAudit(ProceedingJoinPoint joinPoint,
                           Audit audit) throws Throwable {

        long start = System.currentTimeMillis();

        String username = getUsername();

        String ip = getClientIp();

        String userAgent = request.getHeader("User-Agent");
        
        try {

    Object result = joinPoint.proceed();

    String details = messageBuilder.buildMessage(
            audit,
            joinPoint.getArgs(),
            result);

    long execution = System.currentTimeMillis() - start;

    auditService.log(
            username,
            audit.action(),
            "SUCCESS",
            details,
            ip,
            userAgent,
            execution);

    return result;

}
catch (Exception ex) {

    long execution = System.currentTimeMillis() - start;

    String details = messageBuilder.buildMessage(
            audit,
            joinPoint.getArgs(),
            null);

    details += " | Exception: " + ex.getMessage();

    auditService.log(
            username,
            audit.action(),
            "FAILED",
            details,
            ip,
            userAgent,
            execution);

    throw ex;
}

    }

    /**
     * Logged in username.
     */
    private String getUsername() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (auth != null && auth.isAuthenticated()) {

            return auth.getName();
        }

        return "Anonymous";
    }

    /**
     * Client IP Address.
     */
    private String getClientIp() {

        String forwarded =
                request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {

            return forwarded.split(",")[0];
        }

        return request.getRemoteAddr();
    }

}