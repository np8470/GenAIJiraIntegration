package com.genai.ollamarestapi.controller;

import com.genai.ollamarestapi.dto.DashboardStatisticsResponse;
import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.service.DashboardService;
import com.genai.ollamarestapi.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private final UserService userService;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        User user = userService.getCurrentUser();

        DashboardStatisticsResponse statistics =
                dashboardService.statistics(user);

        model.addAttribute(
                "statistics",
                statistics);

        return "dashboard";

    }

}