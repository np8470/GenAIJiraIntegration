package com.genai.ollamarestapi.util;

import java.util.UUID;

public class TraceIdUtil {

    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
