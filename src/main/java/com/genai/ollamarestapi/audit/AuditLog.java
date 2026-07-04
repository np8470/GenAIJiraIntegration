package com.genai.ollamarestapi.audit;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Logged in user
     */
    @Column(nullable = false, length = 100)
    private String username;

    /**
     * LOGIN, LOGOUT, GENERATE_TEST_CASE, etc.
     */
    @Column(nullable = false, length = 50)
    private String action;

    /**
     * SUCCESS / FAILED
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * Extra details
     */
    @Column(length = 2000)
    private String details;

    /**
     * Client IP Address
     */
    @Column(length = 100)
    private String ipAddress;

    /**
     * Browser / Client
     */
    @Column(length = 500)
    private String userAgent;

    /**
     * Method execution time
     */
    private Long executionTimeMs;

    /**
     * Event timestamp
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public AuditLog() {
    }

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    // ---------- Getters ----------

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // ---------- Setters ----------

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", action='" + action + '\'' +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}