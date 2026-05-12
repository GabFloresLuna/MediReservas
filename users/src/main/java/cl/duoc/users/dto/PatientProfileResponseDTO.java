package cl.duoc.users.dto;

import java.math.BigDecimal;

public record PatientProfileResponseDTO(

    Long patientProfileId,
    Long userId,
    String healthInsurance,
    String emergencyContactName,
    String emergencyContactPhone,
    String bloodType,
    String allergies,
    BigDecimal weight

) {}