package com.genai.ollamarestapi.service;

import org.springframework.stereotype.Service;

import com.google.common.base.Supplier;

@Service
public class RetryService {

    public <T> T execute(Supplier<T> action, int retryCount) {

        for (int i = 0; i < retryCount; i++) {
            try {
                return action.get();
            } catch (Exception e) {
                if (i == retryCount - 1) throw e;
                try {
                    Thread.sleep(1000L * (i + 1));
                } catch (InterruptedException ignored) {}
            }
        }
        return null;
    }
}
