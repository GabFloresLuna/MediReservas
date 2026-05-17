package cl.duoc.notifications.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.notifications.model.NotificationTemplate;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long>{

    Optional<NotificationTemplate> findByTemplateCode(String code);

    List<NotificationTemplate> findByActiveTrue();

    Optional<NotificationTemplate> findByTemplateCodeAndActiveTrue(String code);

    boolean existsByTemplateCode(String code);
    
}
