package com.genai.ollamarestapi.model;

import java.util.List;

import com.genai.ollamarestapi.audit.AuditLog;

public class DashboardDTO {

    private long totalAiGenerations;

    private long totalUploads;

    private long totalAuditLogs;

    private long totalUsers;

    private List<AuditLog> recentLogs;

    public long getTotalAiGenerations() {
        return totalAiGenerations;
    }

    public void setTotalAiGenerations(long totalAiGenerations) {
        this.totalAiGenerations = totalAiGenerations;
    }

    public long getTotalUploads() {
        return totalUploads;
    }

    public void setTotalUploads(long totalUploads) {
        this.totalUploads = totalUploads;
    }

    public long getTotalAuditLogs() {
        return totalAuditLogs;
    }

    public void setTotalAuditLogs(long totalAuditLogs) {
        this.totalAuditLogs = totalAuditLogs;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public List<AuditLog> getRecentLogs() {
        return recentLogs;
    }

    public void setRecentLogs(List<AuditLog> recentLogs) {
        this.recentLogs = recentLogs;
    }

}
