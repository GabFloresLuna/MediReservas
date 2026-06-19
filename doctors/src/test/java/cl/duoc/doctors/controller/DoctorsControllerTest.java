package cl.duoc.doctors.controller;

import cl.duoc.doctors.dto.DoctorsDTO;
import cl.duoc.doctors.exception.GlobalExceptionHandler;
import cl.duoc.doctors.service.DoctorsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DoctorsControllerStandaloneTest {

    private MockMvc mockMvc;
    private DoctorsService doctorsService;

    @BeforeEach
    void setup() {
        doctorsService = mock(DoctorsService.class);
        
        DoctorsController doctorsController = new DoctorsController(doctorsService);
        
        // Configurar MockMvc con el controlador y el GlobalExceptionHandler
        mockMvc = MockMvcBuilders
                .standaloneSetup(doctorsController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllDoctors_ShouldReturnListOfDoctors() throws Exception {
        // Arrange
        DoctorsDTO doctor1 = new DoctorsDTO();
        doctor1.setDoctorId(1L);
        doctor1.setUserId(100L);
        doctor1.setMedicalLicenseNumber("LIC-12345");
        doctor1.setActive(true);
        doctor1.setSpecialtyIds(Arrays.asList(1L, 2L));

        DoctorsDTO doctor2 = new DoctorsDTO();
        doctor2.setDoctorId(2L);
        doctor2.setUserId(101L);
        doctor2.setMedicalLicenseNumber("LIC-67890");
        doctor2.setActive(true);
        doctor2.setSpecialtyIds(Arrays.asList(3L));

        List<DoctorsDTO> doctorsList = Arrays.asList(doctor1, doctor2);

        when(doctorsService.findAll()).thenReturn(doctorsList);

        // Act & Assert
        mockMvc.perform(get("/api/v2/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].doctorId").value(1L))
                .andExpect(jsonPath("$[0].medicalLicenseNumber").value("LIC-12345"))
                .andExpect(jsonPath("$[1].doctorId").value(2L))
                .andExpect(jsonPath("$[1].medicalLicenseNumber").value("LIC-67890"));
    }

    @Test
    void getDoctorById_ShouldReturnDoctor_WhenExists() throws Exception {
        // Arrange
        Long doctorId = 1L;
        DoctorsDTO doctor = new DoctorsDTO();
        doctor.setDoctorId(doctorId);
        doctor.setUserId(100L);
        doctor.setMedicalLicenseNumber("LIC-12345");
        doctor.setActive(true);
        doctor.setSpecialtyIds(Arrays.asList(1L, 2L));

        when(doctorsService.findById(doctorId)).thenReturn(doctor);

        // Act & Assert
        mockMvc.perform(get("/api/v2/doctors/{id}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(1L))
                .andExpect(jsonPath("$.medicalLicenseNumber").value("LIC-12345"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void getDoctorById_ShouldReturnNotFound_WhenDoctorDoesNotExist() throws Exception {
        // Arrange
        Long doctorId = 999L;
        when(doctorsService.findById(doctorId))
                .thenThrow(new RuntimeException("Doctor no encontrado"));

        // Act & Assert
        mockMvc.perform(get("/api/v2/doctors/{id}", doctorId))
                .andExpect(status().isNotFound())  // 404
                .andExpect(jsonPath("$.message").value("Doctor no encontrado"));
    }
}