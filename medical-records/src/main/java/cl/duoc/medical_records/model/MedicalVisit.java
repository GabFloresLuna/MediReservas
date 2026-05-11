package cl.duoc.medical_records.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne; 
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "medical_visits")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalVisit 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_visit_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "visit_date",nullable = false)
    private LocalDateTime visitDate;

    @Column(name = "visit_reason", length = 255)
    private String visitReason;

    @Column(name = "treatment",columnDefinition = "text" )
    private String treatment;

}
