package cl.duoc.specialties.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.specialties.model.Specialty;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

    Optional<Specialty> findBySpecialtyName(String specialtyName);

    boolean existsBySpecialtyName(String specialtyName);

    boolean existsBySpecialtyIdAndActiveTrue(Long specialtyId);
}