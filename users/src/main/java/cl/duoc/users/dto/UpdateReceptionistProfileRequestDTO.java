package cl.duoc.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateReceptionistProfileRequestDTO(

        @NotBlank(message = "El turno es obligatorio")
        @Size(max = 30, message = "El turno no puede superar los 30 caracteres")
        String shift,

        @NotBlank(message = "El departamento es obligatorio")
        @Size(max = 80, message = "El departamento no puede superar los 80 caracteres")
        String department

) {}