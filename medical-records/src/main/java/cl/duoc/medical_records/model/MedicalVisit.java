package cl.duoc.medical_records.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Column(name = "observations",columnDefinition = "TEXT" )
    private String observations;

    @Column(name = "treatment",columnDefinition = "TEXT", nullable = false)
    private String treatment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "medicalVisit")
    private List<Diagnoses> diagnoses;
    
    @OneToMany(mappedBy = "medicalVisit")
    private List<VitalSigns> vitalSigns;
}
