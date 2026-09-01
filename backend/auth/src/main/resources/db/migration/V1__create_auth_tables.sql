CREATE TABLE auth_users (
    auth_user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE auth_user_roles (
    auth_user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (auth_user_id, role_id),

    CONSTRAINT fk_auth_user_roles_auth_user
        FOREIGN KEY (auth_user_id)
        REFERENCES auth_users(auth_user_id),

    CONSTRAINT fk_auth_user_roles_role
        FOREIGN KEY (role_id)
        REFERENCES roles(role_id)
);