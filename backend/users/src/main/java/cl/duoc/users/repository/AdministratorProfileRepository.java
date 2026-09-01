package cl.duoc.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.users.model.AdministratorProfile;

public interface AdministratorProfileRepository extends JpaRepository<AdministratorProfile, Long> {

    Optional<AdministratorProfile> findByUserUserId(Long userId);

    boolean existsByUserUserId(Long userId);
}