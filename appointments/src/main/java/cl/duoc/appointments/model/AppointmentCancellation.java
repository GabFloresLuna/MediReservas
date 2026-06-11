package cl.duoc.appointments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "appointment_cancellations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCancellation {

    @Id
    @Column(name = "appointment_cancellation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentCancellationId;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    // ID externo: quien realizó la cancelación
    @Column(name = "cancelled_by_user_id", nullable = false)
    private Long cancelledByUserId;

    @Column(name = "cancellation_reason", length = 255)
    private String cancellationReason;

    @Column(name = "cancelled_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime cancelledAt;
}
