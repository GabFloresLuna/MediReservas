package cl.duoc.users.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record AuthUserResponseDTO(

        Long authUserId,
        String email,
        boolean enabled,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        List<String> roles

) {}