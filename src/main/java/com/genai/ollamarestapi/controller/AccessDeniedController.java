package com.genai.ollamarestapi.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.genai.ollamarestapi.audit.Audit;
import com.genai.ollamarestapi.audit.AuditAction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AccessDeniedController {

    /**
     * 403 Access Denied Page
     */
    @Audit(action = AuditAction.ACCESS_DENIED, message = "You do not have permission to access this resource.")
    @GetMapping("/access-denied")
    public String accessDenied(Model model,
                               Principal principal) {

        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }

        model.addAttribute("message",
                "You do not have permission to access this resource.");

        log.info("Access Denied page");        

        return "access-denied";
    }

}