package cl.duoc.doctors.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.doctors.dto.DoctorsDTO;
import cl.duoc.doctors.model.DoctorSpecialties;
import cl.duoc.doctors.model.Doctors;
import cl.duoc.doctors.repository.DoctorsRepository;
import cl.duoc.doctors.repository.DoctorsSpecialtiesRepository;
import jakarta.transaction.Transactional;

@Service
public class DoctorsService {

    private final DoctorsRepository doctorsRepository;
    private final DoctorsSpecialtiesRepository doctorSpecialtiesRepository;

    public DoctorsService(DoctorsRepository doctorsRepository, DoctorsSpecialtiesRepository doctorSpecialtiesRepository) {
        this.doctorsRepository = doctorsRepository;
        this.doctorSpecialtiesRepository = doctorSpecialtiesRepository;
    }

    // --- OBTENER TODOS ---
    public List<DoctorsDTO> findAll() {
        return doctorsRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // --- OBTENER POR ID ---
    public DoctorsDTO findById(Long id) {
        Doctors doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        return convertToDTO(doctor);
    }

    // --- CREAR ---
    @Transactional
    public DoctorsDTO save(DoctorsDTO dto) {
        Doctors doctor = new Doctors();
        doctor.setUserId(dto.getUserId());
        doctor.setMedicalLicenseNumber(dto.getMedicalLicenseNumber());
        doctor.setActive(true);
        
        Doctors savedDoctor = doctorsRepository.save(doctor);
        // Aquí podrías agregar lógica para guardar en doctor_specialties si fuera necesario
        
        return convertToDTO(savedDoctor);
    }

    // --- ELIMINAR (Lógico) ---
    @Transactional
    public void delete(Long id) {
        Doctors doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
        doctor.setActive(false);
        doctorsRepository.save(doctor);
    }

    // --- Actualizar
    @Transactional
    public DoctorsDTO update(Long id, DoctorsDTO dto) {
        Doctors doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        doctor.setMedicalLicenseNumber(dto.getMedicalLicenseNumber());
        // Actualizamos otros campos si es necesario
        
        // Actualizar especialidades: Borramos las anteriores y guardamos las nuevas
        doctorSpecialtiesRepository.deleteByDoctor(doctor); 
        if (dto.getSpecialtyIds() != null) {
            saveDoctorSpecialties(doctor, dto.getSpecialtyIds());
        }

        return convertToDTO(doctorsRepository.save(doctor));
    }

    // --- MÉTODOS AUXILIARES ---

    private void saveDoctorSpecialties(Doctors doctor, List<Long> specialtyIds) {
        List<DoctorSpecialties> specialties = specialtyIds.stream().map(specialtyId -> {
            DoctorSpecialties ds = new DoctorSpecialties();
            ds.setDoctor(doctor); // Aquí usas la relación ManyToOne
            ds.setSpecialtyId(specialtyId);
            ds.setPrimary(false); // Valor por defecto o lógica según necesites
            return ds;
        }).toList();

        doctorSpecialtiesRepository.saveAll(specialties);
    }

    // --- MÉTODOS DE CONVERSIÓN (MAPPING) ---
    private DoctorsDTO convertToDTO(Doctors doctor) {
        DoctorsDTO dto = new DoctorsDTO();
        dto.setDoctorId(doctor.getDoctorId());
        dto.setUserId(doctor.getUserId());
        dto.setMedicalLicenseNumber(doctor.getMedicalLicenseNumber());
        dto.setActive(doctor.getActive());
        
        // Mapeamos la lista de objetos DoctorSpecialty a una lista de Long (los IDs de las especialidades)
        if (doctor.getSpecialties() != null) {
            List<Long> ids = doctor.getSpecialties().stream()
                    .map(ds -> ds.getSpecialtyId()) // Asumiendo que DoctorSpecialty tiene specialtyId
                    .toList();
            dto.setSpecialtyIds(ids);
        }
        
        return dto;
    }
    

}
