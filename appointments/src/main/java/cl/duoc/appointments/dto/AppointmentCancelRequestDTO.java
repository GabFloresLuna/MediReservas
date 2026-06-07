package cl.duoc.appointments.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

// DTO específico para PATCH /cancel
// Se diferencia de AppointmentStatusChangeRequestDTO porque:
// - cancelledByUserId es @NotNull (obligatorio saber quién canceló)
// - genera registro en appointment_cancellations además del historial
@Data
public class AppointmentCancelRequestDTO {

    @NotNull(message = "El ID del usuario que cancela es obligatorio")
    @Positive(message = "El ID del usuario que cancela debe ser mayor a cero")
    private Long cancelledByUserId;

    @Size(max = 255, message = "La razón de cancelación debe tener máximo 255 caracteres")
    private String cancellationReason;
}
