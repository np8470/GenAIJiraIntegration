package com.genai.ollamarestapi.audit;

/**
 * Enterprise Audit Actions
 *
 * All auditable operations in the application should be added here.
 */
public enum AuditAction {

    // ==========================
    // Authentication
    // ==========================

    LOGIN,
    LOGOUT,
    LOGIN_FAILED,
    ACCESS_DENIED,

    // ==========================
    // AI Operations
    // ==========================
    CREATE_TEST_CASE,
    GENERATE_TEST_CASE,
    REGENERATE_TEST_CASE,
    REVIEW_TEST_CASE,
    FORMAT_TEST_CASE,
    LINK_ISSUE,
    GENERATE,
    BUILD_PROMPT,
    MANUAL_PROMPT,
    API_PROMPT,
    SELENIUM_SCRIPT_PROMPT,
    PARSE_OLLAMA_RES_TO_TESTCASE,
    PARSE_JSON_OBJ_TO_TESTCASE,
    GENERATE_RESPONSE,
    UPLOAD_SELECTED_TO_JIRA,

    // ==========================
    // Jira Operations
    // ==========================

    FETCH_WORK_ITEM,
    UPLOAD_TO_JIRA,
    LINK_TO_WORK_ITEM,

    // ==========================
    // User Management
    // ==========================

    CREATE_USER,
    UPDATE_USER,
    DELETE_USER,

    // ==========================
    // Role Management
    // ==========================

    CREATE_ROLE,
    UPDATE_ROLE,
    DELETE_ROLE,
    ASSIGN_ROLE,

    // ==========================
    // Reports
    // ==========================

    EXPORT_REPORT,
    EXPORT_EXCEL,
    EXPORT_PDF,

    // ==========================
    // Dashboard
    // ==========================

    VIEW_DASHBOARD,
    VIEW_AUDIT_LOGS,

    // ==========================
    // System
    // ==========================

    SYSTEM_ERROR,
    CONFIGURATION_CHANGE,

    // ==========================
    // Future Features
    // ==========================

    TEAM_CREATED,
    TEAM_UPDATED,
    TEAM_DELETED,

    PROJECT_CREATED,
    PROJECT_UPDATED,
    PROJECT_DELETED,


}