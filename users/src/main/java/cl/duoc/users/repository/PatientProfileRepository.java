package cl.duoc.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.users.model.PatientProfile;

public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {

    Optional<PatientProfile> findByUserUserId(Long userId);

    boolean existsByUserUserId(Long userId);
}