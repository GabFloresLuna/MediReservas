CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auth_user_id BIGINT NOT NULL UNIQUE,
    run VARCHAR(12) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_profiles (
    user_profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    phone VARCHAR(20),
    birth_date DATE,
    address VARCHAR(150),

    CONSTRAINT fk_user_profiles_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);

CREATE TABLE patient_profiles (
    patient_profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    health_insurance VARCHAR(80),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    blood_type VARCHAR(10),
    allergies VARCHAR(255),
    weight DECIMAL(5,2),

    CONSTRAINT fk_patient_profiles_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);

CREATE TABLE receptionist_profiles (
    receptionist_profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    shift VARCHAR(30),
    department VARCHAR(80),

    CONSTRAINT fk_receptionist_profiles_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);

CREATE TABLE administrator_profiles (
    administrator_profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    department VARCHAR(80),
    position_name VARCHAR(80),

    CONSTRAINT fk_administrator_profiles_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
);