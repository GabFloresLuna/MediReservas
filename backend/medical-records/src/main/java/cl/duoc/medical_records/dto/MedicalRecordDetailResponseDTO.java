package cl.duoc.medical_records.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;


public record MedicalRecordDetailResponseDTO
(
    Long medicalRecordId,
    Long patientUserId,
    boolean active,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt,
    List<MedicalVisitDetailReponseDTO> visits
) {}
