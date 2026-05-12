package cl.duoc.notifications.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
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
public class notifications {

    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column(name = "user_id", nullable = false)
    private Long  userId;

    @Column(name = "notification_template_id")
    private Long notificationTemplateId;

    @Column(name = "notification_channel", nullable = false)
    @Max(value = 30)
    private String notificationChannel;

    @Column(name = "notification_title", nullable = false)
    @Max(value = 100)
    private String notificationTitle;

    @Column(name = "notification_message", nullable =  false, columnDefinition = "TEXT")
    private String notificationMessage;

    @Column(name = "notification_status", nullable = false)
    @Max(30)
    private String notificationStatus;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "generated_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime currentTimeStamp;

    /* crear la conexión 1 -> n con templates, no olvidares */

}
