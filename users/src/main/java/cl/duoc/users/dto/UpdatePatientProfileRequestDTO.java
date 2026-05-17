package cl.duoc.users.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

public record UpdatePatientProfileRequestDTO(

        @Size(max = 80, message = "La previsión no puede superar los 80 caracteres")
        String healthInsurance,

        @Size(max = 100, message = "El nombre del contacto de emergencia no puede superar los 100 caracteres")
        String emergencyContactName,

        @Size(max = 20, message = "El teléfono de emergencia no puede superar los 20 caracteres")
        String emergencyContactPhone,

        @Size(max = 10, message = "El grupo sanguíneo no puede superar los 10 caracteres")
        String bloodType,

        @Size(max = 255, message = "Las alergias no pueden superar los 255 caracteres")
        String allergies,

        @DecimalMin(value = "1.00", message = "El peso debe ser mayor a 0 kg")
        @DecimalMax(value = "400.00", message = "El peso no puede superar los 400 kg")
        BigDecimal weight

) {}