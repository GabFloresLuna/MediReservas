package cl.duoc.medical_records.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record VitalSignResponseDTO
(
    Long vitalSignId,
    Long medicalVisitId,
    BigDecimal temperature,
    String bloodPressure,
    Integer heartRate,
    BigDecimal weight,
    BigDecimal height,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt
) {}
