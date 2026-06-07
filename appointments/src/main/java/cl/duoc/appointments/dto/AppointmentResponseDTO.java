package cl.duoc.appointments.dto;

import cl.duoc.appointments.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Mismo patrón que NotificationResponseDTO:
// - @Builder para construirlo desde el service con .builder()...build()
// - @JsonFormat en los campos de fecha para formato legible en la respuesta JSON
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {

    private Long appointmentId;
    private Long patientUserId;
    private Long doctorId;
    private Long specialtyId;
    private Long scheduleSlotId;
    private AppointmentStatus appointmentStatus;
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
