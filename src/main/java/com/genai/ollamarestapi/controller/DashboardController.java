package com.genai.ollamarestapi.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.genai.ollamarestapi.service.DashboardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model,
                            Principal principal) {

        model.addAttribute(
                "dashboard",
                dashboardService.getDashboard());

        model.addAttribute(
                "username",
                principal.getName());
        log.info("Admin-dashboard page");
        return "admin-dashboard";
    }

}