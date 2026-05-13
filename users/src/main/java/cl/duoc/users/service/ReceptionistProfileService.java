package cl.duoc.users.service;

import org.springframework.stereotype.Service;

import cl.duoc.users.dto.CreateReceptionistProfileRequestDTO;
import cl.duoc.users.dto.ReceptionistProfileResponseDTO;
import cl.duoc.users.model.ReceptionistProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceptionistProfileService {

    private final ReceptionistProfileRepository receptionistProfileRepository;
    private final UserService userService;

    public ReceptionistProfileResponseDTO createReceptionistProfile(CreateReceptionistProfileRequestDTO request) {

        if (receptionistProfileRepository.existsByUserUserId(request.userId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de recepcionista");
        }

        User user = userService.findUserEntityById(request.userId());

        ReceptionistProfile profile = new ReceptionistProfile();
        profile.setUser(user);
        profile.setShift(request.shift());
        profile.setDepartment(request.department());

        ReceptionistProfile savedProfile = receptionistProfileRepository.save(profile);

        return toResponseDTO(savedProfile);
    }

    public ReceptionistProfileResponseDTO getReceptionistProfileByUserId(Long userId) {
        ReceptionistProfile profile = receptionistProfileRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de recepcionista no encontrado"));

        return toResponseDTO(profile);
    }

    public ReceptionistProfileResponseDTO toResponseDTO(ReceptionistProfile profile) {
        return new ReceptionistProfileResponseDTO(
                profile.getReceptionistProfileId(),
                profile.getUser().getUserId(),
                profile.getShift(),
                profile.getDepartment()
        );
    }
}