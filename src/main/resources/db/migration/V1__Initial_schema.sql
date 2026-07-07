CREATE TABLE users
(
    id BIGSERIAL PRIMARY KEY,

    username VARCHAR(50) UNIQUE NOT NULL,

    email VARCHAR(100) UNIQUE NOT NULL,

    password VARCHAR(255) NOT NULL,

    full_name VARCHAR(100),

    enabled BOOLEAN NOT NULL DEFAULT TRUE,

    account_locked BOOLEAN NOT NULL DEFAULT FALSE,

    account_expired BOOLEAN NOT NULL DEFAULT FALSE,

    credentials_expired BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    last_login TIMESTAMP
);

CREATE TABLE roles
(
    id BIGSERIAL PRIMARY KEY,

    name VARCHAR(50) UNIQUE NOT NULL,

    description VARCHAR(255)
);

CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,

    role_id BIGINT NOT NULL,

    PRIMARY KEY(user_id, role_id),

    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
        REFERENCES users(id),

    CONSTRAINT fk_role
        FOREIGN KEY(role_id)
        REFERENCES roles(id)
);