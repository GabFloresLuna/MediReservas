package cl.duoc.notifications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.notifications.model.Notification;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{
    
    List<Notification> findByUserId(Long userId);

    List<Notification> findByNotificationStatus(String status);

    List<Notification> findByUserIdAndNotificationStatus(Long userId, String status);

    List<Notification> findBySentAtIsNull();

    List<Notification> findByNotificationTemplate(cl.duoc.notifications.model.NotificationTemplate template);

    long countByNotificationStatus(String status);
}
