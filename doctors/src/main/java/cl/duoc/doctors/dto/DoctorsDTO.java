package cl.duoc.doctors.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorsDTO {

    private Long doctorId;
    private Long userId;
    private String medicalLicenseNumber;
    private boolean active;
    private List<Long> specialtyIds;
}
