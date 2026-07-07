CREATE TABLE generation_test_case
(
    id BIGSERIAL PRIMARY KEY,

    history_id BIGINT NOT NULL,

    title VARCHAR(500),

    description TEXT,

    priority VARCHAR(30),

    type VARCHAR(50),

    precondition TEXT,

    steps TEXT,

    expected_result TEXT,

    uploaded_to_jira BOOLEAN DEFAULT FALSE,

    jira_testcase_key VARCHAR(50),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_generation_history
        FOREIGN KEY(history_id)
        REFERENCES generation_history(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_generation_testcase_history
ON generation_test_case(history_id);

CREATE INDEX idx_generation_testcase_upload
ON generation_test_case(uploaded_to_jira);