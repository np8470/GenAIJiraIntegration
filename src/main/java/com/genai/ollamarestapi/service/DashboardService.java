package com.genai.ollamarestapi.service;

import org.springframework.stereotype.Service;

import com.genai.ollamarestapi.audit.AuditAction;
import com.genai.ollamarestapi.audit.AuditRepository;
import com.genai.ollamarestapi.model.DashboardDTO;
import com.genai.ollamarestapi.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardService {

    private final AuditRepository auditRepository;
    private final UserRepository userRepository;

    public DashboardService(AuditRepository auditRepository,
                            UserRepository userRepository) {

        this.auditRepository = auditRepository;
        this.userRepository = userRepository;
    }

    public DashboardDTO getDashboard() {

        DashboardDTO dto = new DashboardDTO();

        dto.setTotalUsers(userRepository.count());

        dto.setTotalAuditLogs(auditRepository.count());

        dto.setTotalAiGenerations(
                auditRepository.countByAction(
                        AuditAction.GENERATE_TEST_CASE.name()));

        dto.setTotalUploads(
                auditRepository.countByAction(
                        AuditAction.UPLOAD_TO_JIRA.name()));

        dto.setRecentLogs(
                auditRepository.findTop20ByOrderByTimestampDesc());

        return dto;

    }

}