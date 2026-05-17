package cl.duoc.auth.dto;

import java.util.List;

public record AuthResponseDTO(

        Long authUserId,
        String email,
        String token,
        List<String> roles

) {}