package cl.duoc.appointments.model;

import cl.duoc.appointments.enums.AppointmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @Column(name = "appointment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    
    @Column(name = "patient_user_id", nullable = false)
    private Long patientUserId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "specialty_id", nullable = false)
    private Long specialtyId;

    @Column(name = "schedule_slot_id", nullable = false)
    private Long scheduleSlotId;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status", nullable = false, length = 30)
    private AppointmentStatus appointmentStatus = AppointmentStatus.PENDING;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "appointment")
    private List<AppointmentStatusHistory> statusHistory = new ArrayList<>();

    @OneToOne(mappedBy = "appointment")
    private AppointmentCancellation cancellation;
}
