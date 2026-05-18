package cl.duoc.medical_records.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VitalSignResponseDTO
(
    Long vitalSignId,
    Long medicalVisitId,
    BigDecimal temperature,
    String bloodPressure,
    Integer heartRate,
    BigDecimal weight,
    BigDecimal height,
    LocalDateTime createdAt
) {}
