package cl.duoc.doctors.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Max;;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctor_id;

    @Column(name = "user_id",nullable = false, unique = true)
    private Long user_id;

    @Column(name = "medical_license_number",nullable = false, unique = true)
    @Max(value = 50,message = "El numero de licencia medica no puede ser mayor a 50 caracteres")
    private String medical_license_number;

    @Column(name = "active",nullable = false, unique = false)
    private Boolean active;

    @Column(name = "created_at",nullable = false, unique = false)
    private Date created_at;
}
