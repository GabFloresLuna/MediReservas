package cl.duoc.users.dto;

public record AdministratorProfileResponseDTO(

    Long administratorProfileId,
    Long userId,
    String department,
    String positionName

) {}