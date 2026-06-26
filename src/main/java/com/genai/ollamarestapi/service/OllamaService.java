package com.genai.ollamarestapi.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaService {
    private final ChatClient chatClient;

    public OllamaService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generate(String prompt) {

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
