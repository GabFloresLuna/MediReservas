CREATE TABLE medical_records (
    medical_record_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_user_id BIGINT NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE medical_visits (
    medical_visit_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_record_id BIGINT NOT NULL,
    appointment_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    visit_date DATETIME NOT NULL,
    visit_reason VARCHAR(255),
    observations TEXT,
    treatment TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_medical_visits_medical_record
        FOREIGN KEY (medical_record_id)
        REFERENCES medical_records(medical_record_id)
);

CREATE TABLE diagnoses (
    diagnosis_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_visit_id BIGINT NOT NULL,
    diagnosis_description VARCHAR(255) NOT NULL,
    diagnosis_notes TEXT,

    CONSTRAINT fk_diagnoses_medical_visit
        FOREIGN KEY (medical_visit_id)
        REFERENCES medical_visits(medical_visit_id)
);

CREATE TABLE vital_signs (
    vital_sign_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_visit_id BIGINT NOT NULL,
    temperature DECIMAL(4,1),
    blood_pressure VARCHAR(20),
    heart_rate INT,
    weight DECIMAL(5,2),
    height DECIMAL(5,2),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vital_signs_medical_visit
        FOREIGN KEY (medical_visit_id)
        REFERENCES medical_visits(medical_visit_id)
);