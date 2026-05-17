package cl.duoc.specialties.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.specialties.dto.CreateSpecialtyRequestDTO;
import cl.duoc.specialties.dto.SpecialtyResponseDTO;
import cl.duoc.specialties.model.Specialty;
import cl.duoc.specialties.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyResponseDTO createSpecialty(CreateSpecialtyRequestDTO request) {

        if (specialtyRepository.existsBySpecialtyName(request.specialtyName())) {
            throw new RuntimeException("Ya existe una especialidad con ese nombre");
        }

        Specialty specialty = new Specialty();
        specialty.setSpecialtyName(request.specialtyName());
        specialty.setDescription(request.description());
        specialty.setActive(true);

        Specialty savedSpecialty = specialtyRepository.save(specialty);

        return toResponseDTO(savedSpecialty);
    }

    public List<SpecialtyResponseDTO> getAllSpecialties() {
        return specialtyRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public SpecialtyResponseDTO getSpecialtyById(Long specialtyId) {
        Specialty specialty = findSpecialtyEntityById(specialtyId);
        return toResponseDTO(specialty);
    }

    public SpecialtyResponseDTO getSpecialtyByName(String specialtyName) {
        Specialty specialty = specialtyRepository.findBySpecialtyName(specialtyName)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        return toResponseDTO(specialty);
    }

    public boolean existsById(Long specialtyId) {
        return specialtyRepository.existsById(specialtyId);
    }

    public boolean existsActiveById(Long specialtyId) {
        return specialtyRepository.existsBySpecialtyIdAndActiveTrue(specialtyId);
    }

    public SpecialtyResponseDTO deactivateSpecialty(Long specialtyId) {
        Specialty specialty = findSpecialtyEntityById(specialtyId);

        if (!specialty.isActive()) {
            throw new RuntimeException("La especialidad ya está desactivada");
        }

        specialty.setActive(false);

        Specialty savedSpecialty = specialtyRepository.save(specialty);

        return toResponseDTO(savedSpecialty);
    }

    public Specialty findSpecialtyEntityById(Long specialtyId) {
        return specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
    }

    private SpecialtyResponseDTO toResponseDTO(Specialty specialty) {
        return new SpecialtyResponseDTO(
                specialty.getSpecialtyId(),
                specialty.getSpecialtyName(),
                specialty.getDescription(),
                specialty.isActive(),
                specialty.getCreatedAt());
    }
}