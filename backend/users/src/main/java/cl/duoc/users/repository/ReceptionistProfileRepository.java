package cl.duoc.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.users.model.ReceptionistProfile;

public interface ReceptionistProfileRepository extends JpaRepository<ReceptionistProfile, Long> {

    Optional<ReceptionistProfile> findByUserUserId(Long userId);

    boolean existsByUserUserId(Long userId);
}