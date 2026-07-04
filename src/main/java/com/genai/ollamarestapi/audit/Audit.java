package com.genai.ollamarestapi.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audit {

    AuditAction action();

    /**
     * Static description.
     * Example:
     * "Generated Test Cases"
     */
    String message() default "";

    /**
     * Whether method parameters should be included.
     */
    boolean includeArguments() default false;

    /**
     * Whether execution result should be included.
     */
    boolean includeResult() default false;

}