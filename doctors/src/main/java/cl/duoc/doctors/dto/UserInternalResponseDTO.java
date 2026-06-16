package cl.duoc.doctors.dto;

public record UserInternalResponseDTO (
    Long userId,
    Long authUserId,
    String run,
    String email,
    boolean active
) {}
