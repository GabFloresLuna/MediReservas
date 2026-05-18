package cl.duoc.notifications.service;

import cl.duoc.notifications.dto.NotificationResponseDTO;
import cl.duoc.notifications.dto.NotificationSendRequestDTO;
import cl.duoc.notifications.model.Notification;
import cl.duoc.notifications.model.NotificationTemplate;
import cl.duoc.notifications.repository.NotificationRepository;
import cl.duoc.notifications.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;

    public NotificationResponseDTO createNotification(NotificationSendRequestDTO dto) {
        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setNotificationChannel(dto.getNotificationChannel());
        notification.setNotificationTitle(dto.getNotificationTitle());
        notification.setNotificationMessage(dto.getNotificationMessage());
        notification.setSentAt(null);

        if (dto.getNotificationTemplateId() != null) {
            NotificationTemplate template = templateRepository.findById(dto.getNotificationTemplateId())
                    .orElseThrow(() -> new RuntimeException(
                            "Id de template no encontrada " + dto.getNotificationTemplateId()));
            notification.setNotificationTemplate(template);
        }

        Notification saved = notificationRepository.save(notification);
        return toResponseDTO(saved);
    }

    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public NotificationResponseDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id no encontrada: " + id));
        return toResponseDTO(notification);
    }

    public List<NotificationResponseDTO> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> getNotificationsByStatus(String status) {
        return notificationRepository.findByNotificationStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> getNotificationsByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return notificationRepository.findByCreatedAtBetween(start, end)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> getNotificationsByUserIdAndStatus(Long userId, String status) {
        return notificationRepository.findByUserIdAndNotificationStatus(userId, status)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private NotificationResponseDTO toResponseDTO(Notification entity) {
        return NotificationResponseDTO.builder()
                .notificationId(entity.getNotificationId())
                .userId(entity.getUserId())
                .notificationChannel(entity.getNotificationChannel())
                .notificationTitle(entity.getNotificationTitle())
                .notificationMessage(entity.getNotificationMessage())
                .notificationStatus(entity.getNotificationStatus())
                .notificationTemplateId(entity.getNotificationTemplate() != null
                        ? entity.getNotificationTemplate().getNotificationTemplateId()
                        : null)
                .sentAt(entity.getSentAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}