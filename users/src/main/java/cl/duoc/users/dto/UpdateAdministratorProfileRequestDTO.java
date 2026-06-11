package cl.duoc.users.dto;

import jakarta.validation.constraints.Size;

public record UpdateAdministratorProfileRequestDTO(

        @Size(max = 80, message = "El departamento no puede superar los 80 caracteres")
        String department,

        @Size(max = 80, message = "El cargo no puede superar los 80 caracteres")
        String positionName

) {}