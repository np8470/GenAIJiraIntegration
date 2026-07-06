package com.genai.ollamarestapi.controller;

import java.security.Principal;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {

    @GetMapping("/login")
    @Audit(action = AuditAction.LOGIN, message = "User authenticated successfully")
    public String login(Authentication authentication) {

        if (authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken)) {
            log.info("User authenticated successfully, redirect to Dashboard Page");
            return "redirect:/dashboard";
        }

        return "login";
    }

    @GetMapping("/")
    @Audit(action = AuditAction.VIEW_DASHBOARD, message = "Redirected to Root")
    public String root() {

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    @Audit(action = AuditAction.VIEW_DASHBOARD, message = "Dashboard viewed")
    public String dashboard(Model model,
                            Principal principal) {

        if (principal != null) {
            model.addAttribute("username",
                               principal.getName());
        }
        log.info("Dashboard Page viewed");
        return "dashboard";
    }

}