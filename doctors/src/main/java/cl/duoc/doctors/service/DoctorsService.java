package cl.duoc.doctors.service;

import java.util.List;
import org.springframework.stereotype.Service;
import cl.duoc.doctors.dto.DoctorsDTO;
import cl.duoc.doctors.model.DoctorSpecialties;
import cl.duoc.doctors.model.Doctors;
import cl.duoc.doctors.repository.DoctorsRepository;
import cl.duoc.doctors.repository.DoctorsSpecialtiesRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j; 
@Service
@Slf4j
public class DoctorsService {

    private final DoctorsRepository doctorsRepository;
    private final DoctorsSpecialtiesRepository doctorSpecialtiesRepository;

    public DoctorsService(DoctorsRepository doctorsRepository, DoctorsSpecialtiesRepository doctorSpecialtiesRepository) {
        this.doctorsRepository = doctorsRepository;
        this.doctorSpecialtiesRepository = doctorSpecialtiesRepository;
    }

    public List<DoctorsDTO> findAll() {
        log.info("Solicitando listado completo de doctores");
        return doctorsRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public DoctorsDTO findById(Long id) {
        log.info("Buscando doctor con ID: {}", id);
        Doctors doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error: No se encontró el doctor con ID: {}", id);
                    return new RuntimeException("Doctor no encontrado");
                });
        return convertToDTO(doctor);
    }

    @Transactional
    public DoctorsDTO save(DoctorsDTO dto) {
        log.info("Registrando nuevo doctor para el usuario ID: {}", dto.getUserId());
        Doctors doctor = new Doctors();
        doctor.setUserId(dto.getUserId());
        doctor.setMedicalLicenseNumber(dto.getMedicalLicenseNumber());
        doctor.setActive(true);
        doctor.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));

        Doctors savedDoctor = doctorsRepository.save(doctor);
        log.info("Doctor guardado exitosamente con ID asignado: {}", savedDoctor.getDoctorId());
        return convertToDTO(savedDoctor);
    }

    @Transactional
    public void delete(Long id) {
        log.warn("Se ha solicitado la desactivación (eliminación lógica) del doctor ID: {}", id);
        Doctors doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Fallo al intentar eliminar: Doctor ID {} no existe", id);
                    return new RuntimeException("Doctor no encontrado");
                });
        doctor.setActive(false);
        doctorsRepository.save(doctor);
        log.info("Doctor ID: {} desactivado correctamente", id);
    }

    @Transactional
    public DoctorsDTO update(Long id, DoctorsDTO dto) {
        log.info("Iniciando actualización para el doctor ID: {}", id);
        Doctors doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Fallo al intentar actualizar: Doctor ID {} no existe", id);
                    return new RuntimeException("Doctor no encontrado");
                });
        
        doctor.setUserId(dto.getUserId());
        doctor.setActive(dto.isActive());
        doctor.setMedicalLicenseNumber(dto.getMedicalLicenseNumber());

        log.debug("Limpiando especialidades anteriores para el doctor ID: {}", id);
        doctorSpecialtiesRepository.deleteByDoctor(doctor); 
        
        if (dto.getSpecialtyIds() != null) {
            log.info("Asociando {} nuevas especialidades al doctor ID: {}", dto.getSpecialtyIds().size(), id);
            saveDoctorSpecialties(doctor, dto.getSpecialtyIds());
        }

        Doctors updatedDoctor = doctorsRepository.save(doctor);
        log.info("Doctor ID: {} actualizado con éxito", id);
        return convertToDTO(updatedDoctor);
    }

    private void saveDoctorSpecialties(Doctors doctor, List<Long> specialtyIds) {
        List<DoctorSpecialties> specialties = specialtyIds.stream().map(specialtyId -> {
            DoctorSpecialties ds = new DoctorSpecialties();
            ds.setDoctor(doctor);
            ds.setSpecialtyId(specialtyId);
            ds.setPrimary(false);
            return ds;
        }).toList();

        doctorSpecialtiesRepository.saveAll(specialties);
    }

    private DoctorsDTO convertToDTO(Doctors doctor) {
        DoctorsDTO dto = new DoctorsDTO();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setUserId(doctor.getUserId());
        dto.setMedicalLicenseNumber(doctor.getMedicalLicenseNumber());
        dto.setActive(doctor.getActive());
        
        if (doctor.getSpecialties() != null) {
            List<Long> ids = doctor.getSpecialties().stream()
                    .map(DoctorSpecialties::getSpecialtyId) 
                    .toList();
            dto.setSpecialtyIds(ids);
        }
        return dto;
    }
}