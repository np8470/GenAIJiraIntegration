package com.genai.ollamarestapi.audit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuditService {

    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    /**
     * Save audit log.
     */
    public AuditLog log(String username,
            String action,
            String status,
            String details,
            String ipAddress,
            String userAgent,
            Long executionTimeMs) {

        AuditLog audit = new AuditLog();

        audit.setUsername(username);
        audit.setAction(action);
        audit.setStatus(status);
        audit.setDetails(details);
        audit.setIpAddress(ipAddress);
        audit.setUserAgent(userAgent);
        audit.setExecutionTimeMs(executionTimeMs);
        audit.setTimestamp(LocalDateTime.now());

        return auditRepository.save(audit);
    }

    /**
     * Convenience method.
     */
    public AuditLog logSuccess(String username,
            String action,
            String details) {

        return log(
                username,
                action,
                "SUCCESS",
                details,
                null,
                null,
                null);
    }

    /**
     * Convenience method.
     */
    public AuditLog logFailure(String username,
            String action,
            String details) {

        return log(
                username,
                action,
                "FAILED",
                details,
                null,
                null,
                null);
    }

    public AuditLog logSuccess(String username,
            AuditAction action,
            String details) {

        return log(
                username,
                action.name(),
                "SUCCESS",
                details,
                null,
                null,
                null);
    }

    public AuditLog logFailure(String username,
            AuditAction action,
            String details) {

        return log(
                username,
                action.name(),
                "FAILED",
                details,
                null,
                null,
                null);
    }

    public AuditLog log(String username,
            AuditAction action,
            String status,
            String details,
            String ipAddress,
            String userAgent,
            Long executionTimeMs) {

        return log(
                username,
                action.name(),
                status,
                details,
                ipAddress,
                userAgent,
                executionTimeMs);
    }

    

    /**
     * Get latest audit logs.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentLogs() {

        return auditRepository.findTop20ByOrderByTimestampDesc();
    }

    /**
     * Get all audit logs.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAllLogs() {

        return auditRepository.findAll();
    }

    /**
     * Find logs by username.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByUsername(String username) {

        return auditRepository
                .findByUsernameOrderByTimestampDesc(username);
    }

    /**
     * Find logs by action.
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getLogsByAction(String action) {

        return auditRepository
                .findByActionOrderByTimestampDesc(action);
    }

    /**
     * Count by action.
     */
    @Transactional(readOnly = true)
    public long countByAction(String action) {

        return auditRepository.countByAction(action);
    }

    /**
     * Count successful operations.
     */
    @Transactional(readOnly = true)
    public long countSuccess() {

        return auditRepository.countByStatus("SUCCESS");
    }

    /**
     * Count failed operations.
     */
    @Transactional(readOnly = true)
    public long countFailure() {

        return auditRepository.countByStatus("FAILED");
    }

    /**
     * Total audit entries.
     */
    @Transactional(readOnly = true)
    public long totalLogs() {

        return auditRepository.count();
    }

}