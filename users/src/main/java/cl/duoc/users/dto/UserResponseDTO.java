package cl.duoc.users.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record UserResponseDTO(

    Long userId,
    Long authUserId,
    String run,
    String email,
    boolean active,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
    LocalDateTime createdAt

) {}