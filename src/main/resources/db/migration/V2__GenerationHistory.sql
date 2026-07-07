CREATE TABLE generation_history
(
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    story_key VARCHAR(100) NOT NULL,

    story_summary TEXT,

    acceptance_criteria TEXT,

    generation_type VARCHAR(30),

    ai_model VARCHAR(50),

    response_time_ms BIGINT,

    test_case_count INT,

    status VARCHAR(20),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_generation_history_user
        FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_generation_history_user
ON generation_history(user_id);

CREATE INDEX idx_generation_history_story
ON generation_history(story_key);

CREATE INDEX idx_generation_history_created
ON generation_history(created_at);