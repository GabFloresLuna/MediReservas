package cl.duoc.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.users.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAuthUserId(Long authUserId);

    Optional<User> findByRun(String run);

    Optional<User> findByEmail(String email);

    boolean existsByAuthUserId(Long authUserId);

    boolean existsByRun(String run);

    boolean existsByEmail(String email);
}