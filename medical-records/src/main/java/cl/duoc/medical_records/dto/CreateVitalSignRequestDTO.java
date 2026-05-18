package cl.duoc.medical_records.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateVitalSignRequestDTO
(
    @NotNull(message = "El ID de la atención médica es obligatorio")
    Long medicalVisitId,

    @DecimalMin(value = "30.0", message = "La temperatura no puede ser menor a 30.0 °C")
    @DecimalMax(value = "45.0", message = "La temperatura no puede ser mayor a 45.0 °C")
    BigDecimal temperature,

    @Pattern(
        regexp = "^\\d{2,3}/\\d{2,3}$",
        message = "La presión arterial debe tener formato 120/80"
    )
    String bloodPressure,

    @Min(value = 30, message = "La frecuencia cardíaca no puede ser menor a 30")
    @Max(value = 220, message = "La frecuencia cardíaca no puede ser mayor a 220")
    Integer heartRate,

    @DecimalMin(value = "1.00", message = "El peso debe ser mayor a 0 kg")
    @DecimalMax(value = "400.00", message = "El peso no puede superar los 400 kg")
    BigDecimal weight,

    @DecimalMin(value = "30.00", message = "La altura no puede ser menor a 30 cm")
    @DecimalMax(value = "250.00", message = "La altura no puede superar los 250 cm")
    BigDecimal height
) {}
