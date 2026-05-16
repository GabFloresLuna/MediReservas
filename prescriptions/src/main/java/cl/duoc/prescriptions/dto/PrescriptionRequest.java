package cl.duoc.prescriptions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionRequest {

    @NotNull
    private Long medicalVisitId;

    @NotNull
    private Long patientUserId;

    @NotNull
    private Long doctorId;

    @NotBlank
    @Size(max = 30)
    private String prescriptionStatus;

    @Size(max = 255)
    private String notes;
}
