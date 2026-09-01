package cl.duoc.users.dto;

public record UserDetailResponseDTO(

    UserResponseDTO user,
    UserProfileResponseDTO profile,
    PatientProfileResponseDTO patientProfile,
    ReceptionistProfileResponseDTO receptionistProfile,
    AdministratorProfileResponseDTO administratorProfile

) {}