package cl.duoc.users.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(

    Long userId,
    Long authUserId,
    String rut,
    String email,
    boolean active,
    LocalDateTime createdAt

) {}