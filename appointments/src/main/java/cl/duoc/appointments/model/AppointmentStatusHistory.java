package cl.duoc.appointments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import cl.duoc.appointments.enums.AppointmentStatus;

@Entity
@Table(name = "appointment_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatusHistory {

    @Id
    @Column(name = "appointment_status_history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentStatusHistoryId;

    @ManyToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status", length = 30)
    private AppointmentStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private AppointmentStatus newStatus;

    // ID externo: el usuario que hizo el cambio (validado via WebClient en el futuro)
    @Column(name = "changed_by_user_id")
    private Long changedByUserId;

    @Column(name = "change_reason", length = 255)
    private String changeReason;

    @Column(name = "changed_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime changedAt;
}
