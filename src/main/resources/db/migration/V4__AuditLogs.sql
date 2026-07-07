CREATE TABLE audit_logs
(
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(100) NOT NULL,

    action VARCHAR(50) NOT NULL,

    status VARCHAR(20) NOT NULL,

    details VARCHAR(2000),

    ip_address VARCHAR(100),

    user_agent VARCHAR(500),

    execution_time_ms BIGINT,

    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_username
ON audit_logs(username);

CREATE INDEX idx_audit_action
ON audit_logs(action);

CREATE INDEX idx_audit_status
ON audit_logs(status);

CREATE INDEX idx_audit_timestamp
ON audit_logs(timestamp);