package cl.duoc.notifications.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notifications {

    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(name = "user_id", nullable = false)
    private Long  userId;

    @ManyToOne
    @JoinColumn(name = "notification_template_id")
    private NotificationTemplates notificationTemplate;

    @Column(name = "notification_channel", nullable = false)
    @Size(max = 30)
    private String notificationChannel;

    @Column(name = "notification_title", nullable = false)
    @Size(max  = 100)
    private String notificationTitle;

    @Column(name = "notification_message", nullable =  false, columnDefinition = "TEXT")
    private String notificationMessage;

    @Column(name = "notification_status", nullable = false)
    @Size(max = 80)
    private String notificationStatus;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /* corroborar que la conexión es correcta */

}
