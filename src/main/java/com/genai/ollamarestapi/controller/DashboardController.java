package com.genai.ollamarestapi.controller;

import com.genai.ollamarestapi.dto.DashboardChartResponse;
import com.genai.ollamarestapi.dto.DashboardStatisticsResponse;
import com.genai.ollamarestapi.dto.RecentGenerationDto;
import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.service.DashboardService;
import com.genai.ollamarestapi.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private final UserService userService;

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {

        User user = userService.getCurrentUser();

        DashboardStatisticsResponse dashboard = dashboardService.statistics(user);

        model.addAttribute("dashboard", dashboard);

        model.addAttribute("username", user.getUsername());

        return "admin-dashboard";
    }

    @GetMapping("/api/dashboard/recent")
    @ResponseBody
    public List<RecentGenerationDto> recent() {

        User user = userService.getCurrentUser();

        return dashboardService.recentGenerations(user);

    }

    @GetMapping("/api/dashboard/charts")
    @ResponseBody
    public DashboardChartResponse charts() {

        User user = userService.getCurrentUser();

        return dashboardService.chartData(user);

    }

}