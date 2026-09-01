package cl.duoc.users.dto;

import java.time.LocalDate;

public record UserProfileResponseDTO(

    Long userProfileId,
    Long userId,
    String firstName,
    String lastName,
    String phone,
    LocalDate birthDate,
    String address

) {}