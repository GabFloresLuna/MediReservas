package cl.duoc.appointments.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

// DTO compartido para PATCH /confirm, /complete y /no-show
// Los tres endpoints solo necesitan saber quién hizo el cambio y por qué,
// el nuevo status lo determina cada método del service
@Data
public class AppointmentStatusChangeRequestDTO {

    @Positive(message = "El ID del usuario que realiza el cambio debe ser mayor a cero")
    private Long changedByUserId;

    @Size(max = 255, message = "La razón debe tener máximo 255 caracteres")
    private String changeReason;
}
