package cl.duoc.doctors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.doctors.model.DoctorSpecialties;

public interface DoctorsSpecialtiesRepository extends JpaRepository<DoctorSpecialties, Long>{

}
