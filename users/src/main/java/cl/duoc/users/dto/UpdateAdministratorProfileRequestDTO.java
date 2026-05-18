package cl.duoc.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateAdministratorProfileRequestDTO(

        @NotBlank(message = "El departamento es obligatorio")
        @Size(max = 80, message = "El departamento no puede superar los 80 caracteres")
        String department,

        @NotBlank(message = "El cargo es obligatorio")
        @Size(max = 80, message = "El cargo no puede superar los 80 caracteres")
        String positionName

) {}