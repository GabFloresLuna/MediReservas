package cl.duoc.doctors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.doctors.model.DoctorSpecialties;
import cl.duoc.doctors.model.Doctors;

public interface DoctorsSpecialtiesRepository extends JpaRepository<DoctorSpecialties, Long>{
    void deleteByDoctor(Doctors doctor);
}
