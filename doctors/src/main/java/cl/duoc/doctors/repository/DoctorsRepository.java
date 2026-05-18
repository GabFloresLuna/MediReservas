package cl.duoc.doctors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.doctors.model.Doctors;

public interface DoctorsRepository extends JpaRepository<Doctors, Long>{

}
