package cl.duoc.users.service;

import org.springframework.stereotype.Service;

import cl.duoc.users.dto.AdministratorProfileResponseDTO;
import cl.duoc.users.dto.CreateAdministratorProfileRequestDTO;
import cl.duoc.users.model.AdministratorProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.AdministratorProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministratorProfileService {

    private final AdministratorProfileRepository administratorProfileRepository;
    private final UserService userService;

    public AdministratorProfileResponseDTO createAdministratorProfile(CreateAdministratorProfileRequestDTO request) {

        if (administratorProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de administrador");
        }

        User user = userService.findUserEntityById(request.userId());

        AdministratorProfile profile = new AdministratorProfile();
        profile.setUser(user);
        profile.setDepartment(request.department());
        profile.setPositionName(request.positionName());

        AdministratorProfile savedProfile = administratorProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public AdministratorProfileResponseDTO getAdministratorProfileByUserId(Long userId) {
        AdministratorProfile profile = administratorProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de administrador no encontrado"));

        return toResponseDTO(profile);
    }

    public AdministratorProfileResponseDTO toResponseDTO(AdministratorProfile profile) {
        return new AdministratorProfileResponseDTO(
                profile.getAdministratorProfileId(),
                profile.getUser().getUserId(),
                profile.getDepartment(),
                profile.getPositionName()
        );
    }
}