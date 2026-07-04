package com.genai.ollamarestapi.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;

@Service
public class OllamaService {
    private final ChatClient chatClient;

    public OllamaService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Audit(action = AuditAction.GENERATE, message = "Generated prompt {0}")
    public String generate(String prompt) {

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
