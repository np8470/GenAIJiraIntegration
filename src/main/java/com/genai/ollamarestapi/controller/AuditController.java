package com.genai.ollamarestapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.genai.ollamarestapi.audit.AuditLog;
import com.genai.ollamarestapi.audit.AuditRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuditController {

    private final AuditRepository auditRepository;

    public AuditController(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping("/admin/audit")
    public String auditLogs(Model model) {

        Page<AuditLog> logs =
                auditRepository.findAll(
                        PageRequest.of(
                                0,
                                100,
                                Sort.by(Sort.Direction.DESC, "timestamp")));

        model.addAttribute("logs", logs.getContent());
        log.info("Audit logs page");  
        return "audit";
    }

}