package cl.duoc.users.dto;

public record ReceptionistProfileResponseDTO(

    Long receptionistProfileId,
    Long userId,
    String shift,
    String department

) {}