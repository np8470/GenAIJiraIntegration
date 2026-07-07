package com.genai.ollamarestapi.controller;

import com.genai.ollamarestapi.dto.GenerationHistoryDetailResponse;
import com.genai.ollamarestapi.dto.GenerationHistoryResponse;
import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.service.GenerationHistoryService;
import com.genai.ollamarestapi.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class GenerationHistoryController {

    private final GenerationHistoryService historyService;
    private final UserService userService;

    @GetMapping
    public Page<GenerationHistoryResponse> history(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        User user = userService.getCurrentUser();

        return historyService.getHistory(
                user,
                PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public GenerationHistoryDetailResponse details(
            @PathVariable Long id) {

        User user = userService.getCurrentUser();

        return historyService.getHistoryDetail(id, user);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {

        User user = userService.getCurrentUser();

        historyService.delete(id, user);
    }
}