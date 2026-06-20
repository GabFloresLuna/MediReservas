package cl.duoc.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.users.dto.CreatePatientProfileRequestDTO;
import cl.duoc.users.dto.PatientProfileResponseDTO;
import cl.duoc.users.dto.UpdatePatientProfileRequestDTO;
import cl.duoc.users.model.PatientProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.AdministratorProfileRepository;
import cl.duoc.users.repository.PatientProfileRepository;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import cl.duoc.users.repository.UserProfileRepository;
import cl.duoc.users.client.AuthClient;

@ExtendWith(MockitoExtension.class)
class PatientProfileServiceTest {

    @Mock
    private PatientProfileRepository patientProfileRepository;

    @Mock
    private ReceptionistProfileRepository receptionistProfileRepository;

    @Mock
    private AdministratorProfileRepository administratorProfileRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private PatientProfileService patientProfileService;

    private User user;
    private PatientProfile patientProfile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setAuthUserId(10L);
        user.setRun("12345678-9");
        user.setEmail("paciente@test.cl");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.of(2026, 6, 18, 12, 0));

        patientProfile = new PatientProfile();
        patientProfile.setPatientProfileId(1L);
        patientProfile.setUser(user);
        patientProfile.setHealthInsurance("Fonasa");
        patientProfile.setEmergencyContactName("María Pérez");
        patientProfile.setEmergencyContactPhone("912345678");
        patientProfile.setBloodType("O+");
        patientProfile.setAllergies("Ninguna");
        patientProfile.setWeight(new BigDecimal("75.50"));
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Test
    void createPatientProfile_deberiaCrearPerfilPacienteCorrectamente() {
        CreatePatientProfileRequestDTO request = new CreatePatientProfileRequestDTO(
                1L,
                "Fonasa",
                "María Pérez",
                "912345678",
                "O+",
                "Ninguna",
                new BigDecimal("75.50"));

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(userService.findUserEntityById(1L))
                .thenReturn(user);

        doNothing()
                .when(authClient)
                .assignRole(10L, "PATIENT");

        when(patientProfileRepository.save(any(PatientProfile.class)))
                .thenAnswer(invocation -> {
                    PatientProfile saved = invocation.getArgument(0);
                    saved.setPatientProfileId(1L);
                    return saved;
                });

        PatientProfileResponseDTO response = patientProfileService.createPatientProfile(request);

        assertNotNull(response);
        assertEquals(1L, response.patientProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Fonasa", response.healthInsurance());
        assertEquals("María Pérez", response.emergencyContactName());
        assertEquals("912345678", response.emergencyContactPhone());
        assertEquals("O+", response.bloodType());
        assertEquals("Ninguna", response.allergies());
        assertEquals(new BigDecimal("75.50"), response.weight());

        ArgumentCaptor<PatientProfile> captor = ArgumentCaptor.forClass(PatientProfile.class);

        verify(patientProfileRepository).save(captor.capture());

        PatientProfile savedProfile = captor.getValue();

        assertEquals(user, savedProfile.getUser());
        assertEquals("Fonasa", savedProfile.getHealthInsurance());
        assertEquals("María Pérez", savedProfile.getEmergencyContactName());
        assertEquals("912345678", savedProfile.getEmergencyContactPhone());
        assertEquals("O+", savedProfile.getBloodType());
        assertEquals("Ninguna", savedProfile.getAllergies());
        assertEquals(new BigDecimal("75.50"), savedProfile.getWeight());

        verify(authClient).assignRole(10L, "PATIENT");
    }

    @Test
    void createPatientProfile_deberiaFallarCuandoUsuarioNoTienePerfilGeneral() {
        CreatePatientProfileRequestDTO request = new CreatePatientProfileRequestDTO(
                1L,
                "Fonasa",
                "María Pérez",
                "912345678",
                "O+",
                "Ninguna",
                new BigDecimal("75.50"));

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientProfileService.createPatientProfile(request));

        assertEquals(
                "El usuario debe tener un perfil general antes de crear un perfil de paciente",
                exception.getMessage());

        verify(patientProfileRepository, never())
                .save(any(PatientProfile.class));
    }

    @Test
    void createPatientProfile_deberiaFallarCuandoUsuarioYaTienePerfilPaciente() {
        CreatePatientProfileRequestDTO request = new CreatePatientProfileRequestDTO(
                1L,
                "Fonasa",
                "María Pérez",
                "912345678",
                "O+",
                "Ninguna",
                new BigDecimal("75.50"));

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientProfileService.createPatientProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de paciente",
                exception.getMessage());

        verify(patientProfileRepository, never())
                .save(any(PatientProfile.class));
    }

    @Test
    void createPatientProfile_deberiaFallarCuandoUsuarioYaTienePerfilRecepcionista() {
        CreatePatientProfileRequestDTO request = new CreatePatientProfileRequestDTO(
                1L,
                "Fonasa",
                "María Pérez",
                "912345678",
                "O+",
                "Ninguna",
                new BigDecimal("75.50"));

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientProfileService.createPatientProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de recepcionista",
                exception.getMessage());

        verify(patientProfileRepository, never())
                .save(any(PatientProfile.class));
    }

    @Test
    void createPatientProfile_deberiaFallarCuandoUsuarioYaTienePerfilAdministrador() {
        CreatePatientProfileRequestDTO request = new CreatePatientProfileRequestDTO(
                1L,
                "Fonasa",
                "María Pérez",
                "912345678",
                "O+",
                "Ninguna",
                new BigDecimal("75.50"));

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientProfileService.createPatientProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de administrador",
                exception.getMessage());

        verify(patientProfileRepository, never())
                .save(any(PatientProfile.class));
    }

    // =========================================================
    // GET
    // =========================================================

    @Test
    void getPatientProfileByUserId_deberiaRetornarPerfilPaciente() {
        when(patientProfileRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(patientProfile));

        PatientProfileResponseDTO response = patientProfileService.getPatientProfileByUserId(1L);

        assertNotNull(response);
        assertEquals(1L, response.patientProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Fonasa", response.healthInsurance());

        verify(patientProfileRepository)
                .findByUserUserId(1L);
    }

    @Test
    void getPatientProfileByUserId_deberiaFallarCuandoNoExiste() {
        when(patientProfileRepository.findByUserUserId(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientProfileService.getPatientProfileByUserId(99L));

        assertEquals(
                "Perfil de paciente no encontrado",
                exception.getMessage());
    }

    @Test
    void getAllPatientProfiles_deberiaRetornarLista() {
        when(patientProfileRepository.findAll())
                .thenReturn(List.of(patientProfile));

        List<PatientProfileResponseDTO> response = patientProfileService.getAllPatientProfiles();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Fonasa", response.get(0).healthInsurance());

        verify(patientProfileRepository)
                .findAll();
    }

    @Test
    void getPatientProfileById_deberiaRetornarPerfilPaciente() {
        when(patientProfileRepository.findById(1L))
                .thenReturn(Optional.of(patientProfile));

        PatientProfileResponseDTO response = patientProfileService.getPatientProfileById(1L);

        assertNotNull(response);
        assertEquals(1L, response.patientProfileId());
        assertEquals(1L, response.userId());
        assertEquals("O+", response.bloodType());

        verify(patientProfileRepository)
                .findById(1L);
    }

    @Test
    void getPatientProfileById_deberiaFallarCuandoNoExiste() {
        when(patientProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientProfileService.getPatientProfileById(99L));

        assertEquals(
                "Perfil de paciente no encontrado",
                exception.getMessage());
    }

    @Test
    void existsByUserId_deberiaRetornarTrue() {
        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        boolean response = patientProfileService.existsByUserId(1L);

        assertTrue(response);

        verify(patientProfileRepository)
                .existsByUserUserId(1L);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void updatePatientProfile_deberiaActualizarPerfilPaciente() {
        UpdatePatientProfileRequestDTO request = new UpdatePatientProfileRequestDTO(
                "Isapre",
                "Carlos Pérez",
                "987654321",
                "A+",
                "Penicilina",
                new BigDecimal("80.25"));

        when(patientProfileRepository.findById(1L))
                .thenReturn(Optional.of(patientProfile));

        when(patientProfileRepository.save(patientProfile))
                .thenReturn(patientProfile);

        PatientProfileResponseDTO response = patientProfileService.updatePatientProfile(1L, request);

        assertNotNull(response);
        assertEquals("Isapre", response.healthInsurance());
        assertEquals("Carlos Pérez", response.emergencyContactName());
        assertEquals("987654321", response.emergencyContactPhone());
        assertEquals("A+", response.bloodType());
        assertEquals("Penicilina", response.allergies());
        assertEquals(new BigDecimal("80.25"), response.weight());

        verify(patientProfileRepository)
                .save(patientProfile);
    }

    @Test
    void updatePatientProfile_deberiaFallarCuandoNoExiste() {
        UpdatePatientProfileRequestDTO request = new UpdatePatientProfileRequestDTO(
                "Isapre",
                "Carlos Pérez",
                "987654321",
                "A+",
                "Penicilina",
                new BigDecimal("80.25"));

        when(patientProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientProfileService.updatePatientProfile(99L, request));

        assertEquals(
                "Perfil de paciente no encontrado",
                exception.getMessage());

        verify(patientProfileRepository, never())
                .save(any(PatientProfile.class));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void deletePatientProfile_deberiaEliminarPerfilPaciente() {
        when(patientProfileRepository.findById(1L))
                .thenReturn(Optional.of(patientProfile));

        patientProfileService.deletePatientProfile(1L);

        verify(patientProfileRepository)
                .delete(patientProfile);
    }

    @Test
    void deletePatientProfile_deberiaFallarCuandoNoExiste() {
        when(patientProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> patientProfileService.deletePatientProfile(99L));

        assertEquals(
                "Perfil de paciente no encontrado",
                exception.getMessage());

        verify(patientProfileRepository, never())
                .delete(any(PatientProfile.class));
    }
}