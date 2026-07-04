package com.genai.ollamarestapi.audit;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, Long> {

    /**
     * Get all audit logs for a specific user.
     */
    List<AuditLog> findByUsername(String username);

    /**
     * Get all audit logs by action.
     */
    List<AuditLog> findByAction(String action);

    /**
     * Get all audit logs by status.
     */
    List<AuditLog> findByStatus(String status);

    /**
     * Get logs for a user ordered by latest first.
     */
    List<AuditLog> findByUsernameOrderByTimestampDesc(String username);

    /**
     * Get logs by action ordered by latest first.
     */
    List<AuditLog> findByActionOrderByTimestampDesc(String action);

    /**
     * Get logs between two dates.
     */
    List<AuditLog> findByTimestampBetween(
            LocalDateTime start,
            LocalDateTime end);

    /**
     * Get logs by username within a date range.
     */
    List<AuditLog> findByUsernameAndTimestampBetween(
            String username,
            LocalDateTime start,
            LocalDateTime end);

    /**
     * Get logs by action within a date range.
     */
    List<AuditLog> findByActionAndTimestampBetween(
            String action,
            LocalDateTime start,
            LocalDateTime end);

    /**
     * Get logs by status within a date range.
     */
    List<AuditLog> findByStatusAndTimestampBetween(
            String status,
            LocalDateTime start,
            LocalDateTime end);

    /**
     * Latest logs.
     */
    List<AuditLog> findTop20ByOrderByTimestampDesc();

    /**
     * Latest logs (paged).
     */
    Page<AuditLog> findAllByOrderByTimestampDesc(Pageable pageable);

    /**
     * Count by user.
     */
    long countByUsername(String username);

    /**
     * Count by action.
     */
    long countByAction(String action);

    /**
     * Count by status.
     */
    long countByStatus(String status);

}