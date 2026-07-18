/* package com.genai.ollamarestapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.genai.ollamarestapi.entity.User;
import com.genai.ollamarestapi.service.UserService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GeneratorController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        User user = userService.getCurrentUser();

        model.addAttribute("username",
                user.getUsername());

        return "dashboard";
    }

}
 */