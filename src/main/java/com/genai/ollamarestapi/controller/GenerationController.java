package com.genai.ollamarestapi.controller;

import com.genai.ollamarestapi.model.GenerateResponse;
import com.genai.ollamarestapi.model.GenerationType;
import com.genai.ollamarestapi.service.TestCaseOrchestratorService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GenerationController {

    private final TestCaseOrchestratorService service;

    @GetMapping("/generate")
    public GenerateResponse generate(
            @RequestParam String storyKey,
            @RequestParam String type,
            HttpSession session) {

        return service.generateOnly(
                storyKey,
                GenerationType.valueOf(type),
                session);
    }

    @PostMapping("/upload")
    public String uploadToJira(HttpSession session) {

        return service.uploadToJira(session);
    }

}