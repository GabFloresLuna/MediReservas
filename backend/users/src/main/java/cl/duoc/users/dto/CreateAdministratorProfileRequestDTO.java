package cl.duoc.users.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAdministratorProfileRequestDTO(

    @NotNull(message = "El ID del usuario es obligatorio")
    Long userId,

    @Size(max = 80, message = "El departamento no puede superar los 80 caracteres")
    String department,

    @Size(max = 80, message = "El cargo no puede superar los 80 caracteres")
    String positionName

) {}