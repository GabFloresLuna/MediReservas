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

import cl.duoc.users.client.AuthClient;
import cl.duoc.users.dto.AdministratorProfileResponseDTO;
import cl.duoc.users.dto.CreateAdministratorProfileRequestDTO;
import cl.duoc.users.dto.UpdateAdministratorProfileRequestDTO;
import cl.duoc.users.model.AdministratorProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.AdministratorProfileRepository;
import cl.duoc.users.repository.PatientProfileRepository;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import cl.duoc.users.repository.UserProfileRepository;

@ExtendWith(MockitoExtension.class)
class AdministratorProfileServiceTest {

    @Mock
    private AdministratorProfileRepository administratorProfileRepository;

    @Mock
    private PatientProfileRepository patientProfileRepository;

    @Mock
    private ReceptionistProfileRepository receptionistProfileRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private AdministratorProfileService administratorProfileService;

    private User user;
    private AdministratorProfile administratorProfile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setAuthUserId(10L);
        user.setRun("12345678-9");
        user.setEmail("admin@test.cl");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.of(2026, 6, 18, 12, 0));

        administratorProfile = new AdministratorProfile();
        administratorProfile.setAdministratorProfileId(1L);
        administratorProfile.setUser(user);
        administratorProfile.setDepartment("Administración");
        administratorProfile.setPositionName("Jefe de operaciones");
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Test
    void createAdministratorProfile_deberiaCrearPerfilAdministradorCorrectamente() {
        CreateAdministratorProfileRequestDTO request = new CreateAdministratorProfileRequestDTO(
                1L,
                "Administración",
                "Jefe de operaciones");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(userService.findUserEntityById(1L))
                .thenReturn(user);

        doNothing()
                .when(authClient)
                .assignRole(10L, "ADMIN");

        when(administratorProfileRepository.save(any(AdministratorProfile.class)))
                .thenAnswer(invocation -> {
                    AdministratorProfile saved = invocation.getArgument(0);
                    saved.setAdministratorProfileId(1L);
                    return saved;
                });

        AdministratorProfileResponseDTO response = administratorProfileService.createAdministratorProfile(request);

        assertNotNull(response);
        assertEquals(1L, response.administratorProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Administración", response.department());
        assertEquals("Jefe de operaciones", response.positionName());

        ArgumentCaptor<AdministratorProfile> captor = ArgumentCaptor.forClass(AdministratorProfile.class);

        verify(administratorProfileRepository).save(captor.capture());

        AdministratorProfile savedProfile = captor.getValue();

        assertEquals(user, savedProfile.getUser());
        assertEquals("Administración", savedProfile.getDepartment());
        assertEquals("Jefe de operaciones", savedProfile.getPositionName());

        verify(authClient).assignRole(10L, "ADMIN");
    }

    @Test
    void createAdministratorProfile_deberiaFallarCuandoUsuarioNoTienePerfilGeneral() {
        CreateAdministratorProfileRequestDTO request = new CreateAdministratorProfileRequestDTO(
                1L,
                "Administración",
                "Jefe de operaciones");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> administratorProfileService.createAdministratorProfile(request));

        assertEquals(
                "El usuario debe tener un perfil general antes de crear un perfil de administrador",
                exception.getMessage());

        verify(administratorProfileRepository, never())
                .save(any(AdministratorProfile.class));
    }

    @Test
    void createAdministratorProfile_deberiaFallarCuandoUsuarioYaTienePerfilAdministrador() {
        CreateAdministratorProfileRequestDTO request = new CreateAdministratorProfileRequestDTO(
                1L,
                "Administración",
                "Jefe de operaciones");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> administratorProfileService.createAdministratorProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de administrador",
                exception.getMessage());

        verify(administratorProfileRepository, never())
                .save(any(AdministratorProfile.class));
    }

    @Test
    void createAdministratorProfile_deberiaFallarCuandoUsuarioYaTienePerfilPaciente() {
        CreateAdministratorProfileRequestDTO request = new CreateAdministratorProfileRequestDTO(
                1L,
                "Administración",
                "Jefe de operaciones");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> administratorProfileService.createAdministratorProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de paciente",
                exception.getMessage());

        verify(administratorProfileRepository, never())
                .save(any(AdministratorProfile.class));
    }

    @Test
    void createAdministratorProfile_deberiaFallarCuandoUsuarioYaTienePerfilRecepcionista() {
        CreateAdministratorProfileRequestDTO request = new CreateAdministratorProfileRequestDTO(
                1L,
                "Administración",
                "Jefe de operaciones");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> administratorProfileService.createAdministratorProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de recepcionista",
                exception.getMessage());

        verify(administratorProfileRepository, never())
                .save(any(AdministratorProfile.class));
    }

    // =========================================================
    // GET
    // =========================================================

    @Test
    void getAdministratorProfileByUserId_deberiaRetornarPerfilAdministrador() {
        when(administratorProfileRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(administratorProfile));

        AdministratorProfileResponseDTO response = administratorProfileService.getAdministratorProfileByUserId(1L);

        assertNotNull(response);
        assertEquals(1L, response.administratorProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Administración", response.department());
        assertEquals("Jefe de operaciones", response.positionName());

        verify(administratorProfileRepository)
                .findByUserUserId(1L);
    }

    @Test
    void getAdministratorProfileByUserId_deberiaFallarCuandoNoExiste() {
        when(administratorProfileRepository.findByUserUserId(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> administratorProfileService.getAdministratorProfileByUserId(99L));

        assertEquals(
                "Perfil de administrador no encontrado",
                exception.getMessage());
    }

    @Test
    void getAllAdministratorProfiles_deberiaRetornarLista() {
        when(administratorProfileRepository.findAll())
                .thenReturn(List.of(administratorProfile));

        List<AdministratorProfileResponseDTO> response = administratorProfileService.getAllAdministratorProfiles();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Administración", response.get(0).department());
        assertEquals("Jefe de operaciones", response.get(0).positionName());

        verify(administratorProfileRepository)
                .findAll();
    }

    @Test
    void getAdministratorProfileById_deberiaRetornarPerfilAdministrador() {
        when(administratorProfileRepository.findById(1L))
                .thenReturn(Optional.of(administratorProfile));

        AdministratorProfileResponseDTO response = administratorProfileService.getAdministratorProfileById(1L);

        assertNotNull(response);
        assertEquals(1L, response.administratorProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Administración", response.department());

        verify(administratorProfileRepository)
                .findById(1L);
    }

    @Test
    void getAdministratorProfileById_deberiaFallarCuandoNoExiste() {
        when(administratorProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> administratorProfileService.getAdministratorProfileById(99L));

        assertEquals(
                "Perfil de administrador no encontrado",
                exception.getMessage());
    }

    @Test
    void existsByUserId_deberiaRetornarTrue() {
        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        boolean response = administratorProfileService.existsByUserId(1L);

        assertTrue(response);

        verify(administratorProfileRepository)
                .existsByUserUserId(1L);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void updateAdministratorProfile_deberiaActualizarPerfilAdministrador() {
        UpdateAdministratorProfileRequestDTO request = new UpdateAdministratorProfileRequestDTO(
                "Dirección",
                "Administrador general");

        when(administratorProfileRepository.findById(1L))
                .thenReturn(Optional.of(administratorProfile));

        when(administratorProfileRepository.save(administratorProfile))
                .thenReturn(administratorProfile);

        AdministratorProfileResponseDTO response = administratorProfileService.updateAdministratorProfile(1L, request);

        assertNotNull(response);
        assertEquals("Dirección", response.department());
        assertEquals("Administrador general", response.positionName());

        verify(administratorProfileRepository)
                .save(administratorProfile);
    }

    @Test
    void updateAdministratorProfile_deberiaFallarCuandoNoExiste() {
        UpdateAdministratorProfileRequestDTO request = new UpdateAdministratorProfileRequestDTO(
                "Dirección",
                "Administrador general");

        when(administratorProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> administratorProfileService.updateAdministratorProfile(99L, request));

        assertEquals(
                "Perfil de administrador no encontrado",
                exception.getMessage());

        verify(administratorProfileRepository, never())
                .save(any(AdministratorProfile.class));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void deleteAdministratorProfile_deberiaEliminarPerfilAdministrador() {
        when(administratorProfileRepository.findById(1L))
                .thenReturn(Optional.of(administratorProfile));

        administratorProfileService.deleteAdministratorProfile(1L);

        verify(administratorProfileRepository)
                .delete(administratorProfile);
    }

    @Test
    void deleteAdministratorProfile_deberiaFallarCuandoNoExiste() {
        when(administratorProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> administratorProfileService.deleteAdministratorProfile(99L));

        assertEquals(
                "Perfil de administrador no encontrado",
                exception.getMessage());

        verify(administratorProfileRepository, never())
                .delete(any(AdministratorProfile.class));
    }
}