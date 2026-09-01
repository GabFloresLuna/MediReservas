package cl.duoc.users.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "auth_user_id", nullable = false, unique = true)
    private Long authUserId;

    @Column(name = "run", nullable = false, unique = true, length = 12)
    private String run;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private UserProfile profile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private PatientProfile patientProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private ReceptionistProfile receptionistProfile;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private AdministratorProfile administratorProfile;
}
