package com.genai.ollamarestapi.controller;

import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.service.GenerationHistoryService;
import com.genai.ollamarestapi.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HistoryPageController {

    private final GenerationHistoryService historyService;
    private final UserService userService;

    @GetMapping("/history")
    public String history(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            Model model) {

        User user = userService.getCurrentUser();

        model.addAttribute(
                "historyPage",
                historyService.getHistory(
                        user,
                        PageRequest.of(page, size)));

        return "history";
    }

}