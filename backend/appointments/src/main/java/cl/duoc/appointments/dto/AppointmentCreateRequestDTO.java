package cl.duoc.appointments.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class AppointmentCreateRequestDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    @Positive(message = "El ID del paciente debe ser mayor a cero")
    private Long patientUserId;

    @NotNull(message = "El ID del médico es obligatorio")
    @Positive(message = "El ID del médico debe ser mayor a cero")
    private Long doctorId;

    @NotNull(message = "El ID de la especialidad es obligatorio")
    @Positive(message = "El ID de la especialidad debe ser mayor a cero")
    private Long specialtyId;

    @NotNull(message = "El ID del bloque de agenda es obligatorio")
    @Positive(message = "El ID del bloque de agenda debe ser mayor a cero")
    private Long scheduleSlotId;

    @Size(max = 255, message = "El motivo debe tener máximo 255 caracteres")
    private String reason;
}
