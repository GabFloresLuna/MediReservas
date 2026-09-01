package cl.duoc.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequestDTO(

        @NotBlank(message = "El RUN es obligatorio")
        @Size(max = 12, message = "El RUN no puede superar los 12 caracteres")
        String run,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe tener un formato válido")
        @Size(max = 100, message = "El correo no puede superar los 100 caracteres")
        String email

) {}