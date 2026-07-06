package com.genai.ollamarestapi.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OllamaService {
    private final ChatClient chatClient;

    public OllamaService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Audit(action = AuditAction.GENERATE, message = "Prompt generated")
    public String generate(String prompt) {
        log.info("Prompt generation");
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
