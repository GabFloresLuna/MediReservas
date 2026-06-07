package cl.duoc.appointments.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class AppointmentCancelRequestDTO {

    @NotNull(message = "El ID del usuario que cancela es obligatorio")
    @Positive(message = "El ID del usuario que cancela debe ser mayor a cero")
    private Long cancelledByUserId;

    @Size(max = 255, message = "La razón de cancelación debe tener máximo 255 caracteres")
    private String cancellationReason;
}
