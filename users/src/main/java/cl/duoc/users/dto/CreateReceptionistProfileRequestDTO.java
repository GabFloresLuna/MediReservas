package cl.duoc.users.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReceptionistProfileRequestDTO(

    @NotNull(message = "El ID del usuario es obligatorio")
    Long userId,

    @Size(max = 30, message = "El turno no puede superar los 30 caracteres")
    String shift,

    @Size(max = 80, message = "El departamento no puede superar los 80 caracteres")
    String department

) {}