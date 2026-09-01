package cl.duoc.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AssignRoleRequestDTO(

        @NotBlank(message = "El rol es obligatorio")
        String roleName

) {}