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
import cl.duoc.users.dto.CreateReceptionistProfileRequestDTO;
import cl.duoc.users.dto.ReceptionistProfileResponseDTO;
import cl.duoc.users.dto.UpdateReceptionistProfileRequestDTO;
import cl.duoc.users.model.ReceptionistProfile;
import cl.duoc.users.model.User;
import cl.duoc.users.repository.AdministratorProfileRepository;
import cl.duoc.users.repository.PatientProfileRepository;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import cl.duoc.users.repository.UserProfileRepository;

@ExtendWith(MockitoExtension.class)
class ReceptionistProfileServiceTest {

    @Mock
    private ReceptionistProfileRepository receptionistProfileRepository;

    @Mock
    private PatientProfileRepository patientProfileRepository;

    @Mock
    private AdministratorProfileRepository administratorProfileRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthClient authClient;

    @InjectMocks
    private ReceptionistProfileService receptionistProfileService;

    private User user;
    private ReceptionistProfile receptionistProfile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setAuthUserId(10L);
        user.setRun("12345678-9");
        user.setEmail("recepcion@test.cl");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.of(2026, 6, 18, 12, 0));

        receptionistProfile = new ReceptionistProfile();
        receptionistProfile.setReceptionistProfileId(1L);
        receptionistProfile.setUser(user);
        receptionistProfile.setShift("Mañana");
        receptionistProfile.setDepartment("Admisión");
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Test
    void createReceptionistProfile_deberiaCrearPerfilRecepcionistaCorrectamente() {
        CreateReceptionistProfileRequestDTO request = new CreateReceptionistProfileRequestDTO(
                1L,
                "Mañana",
                "Admisión");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(userService.findUserEntityById(1L))
                .thenReturn(user);

        doNothing()
                .when(authClient)
                .assignRole(10L, "RECEPTIONIST");

        when(receptionistProfileRepository.save(any(ReceptionistProfile.class)))
                .thenAnswer(invocation -> {
                    ReceptionistProfile saved = invocation.getArgument(0);
                    saved.setReceptionistProfileId(1L);
                    return saved;
                });

        ReceptionistProfileResponseDTO response = receptionistProfileService.createReceptionistProfile(request);

        assertNotNull(response);
        assertEquals(1L, response.receptionistProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Mañana", response.shift());
        assertEquals("Admisión", response.department());

        ArgumentCaptor<ReceptionistProfile> captor = ArgumentCaptor.forClass(ReceptionistProfile.class);

        verify(receptionistProfileRepository).save(captor.capture());

        ReceptionistProfile savedProfile = captor.getValue();

        assertEquals(user, savedProfile.getUser());
        assertEquals("Mañana", savedProfile.getShift());
        assertEquals("Admisión", savedProfile.getDepartment());

        verify(authClient).assignRole(10L, "RECEPTIONIST");
    }

    @Test
    void createReceptionistProfile_deberiaFallarCuandoUsuarioNoTienePerfilGeneral() {
        CreateReceptionistProfileRequestDTO request = new CreateReceptionistProfileRequestDTO(
                1L,
                "Mañana",
                "Admisión");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receptionistProfileService.createReceptionistProfile(request));

        assertEquals(
                "El usuario debe tener un perfil general antes de crear el perfil de recepcionista",
                exception.getMessage());

        verify(receptionistProfileRepository, never())
                .save(any(ReceptionistProfile.class));
    }

    @Test
    void createReceptionistProfile_deberiaFallarCuandoUsuarioYaTienePerfilRecepcionista() {
        CreateReceptionistProfileRequestDTO request = new CreateReceptionistProfileRequestDTO(
                1L,
                "Mañana",
                "Admisión");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receptionistProfileService.createReceptionistProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de recepcionista",
                exception.getMessage());

        verify(receptionistProfileRepository, never())
                .save(any(ReceptionistProfile.class));
    }

    @Test
    void createReceptionistProfile_deberiaFallarCuandoUsuarioYaTienePerfilPaciente() {
        CreateReceptionistProfileRequestDTO request = new CreateReceptionistProfileRequestDTO(
                1L,
                "Mañana",
                "Admisión");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receptionistProfileService.createReceptionistProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de paciente",
                exception.getMessage());

        verify(receptionistProfileRepository, never())
                .save(any(ReceptionistProfile.class));
    }

    @Test
    void createReceptionistProfile_deberiaFallarCuandoUsuarioYaTienePerfilAdministrador() {
        CreateReceptionistProfileRequestDTO request = new CreateReceptionistProfileRequestDTO(
                1L,
                "Mañana",
                "Admisión");

        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receptionistProfileService.createReceptionistProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil de administrador",
                exception.getMessage());

        verify(receptionistProfileRepository, never())
                .save(any(ReceptionistProfile.class));
    }

    // =========================================================
    // GET
    // =========================================================

    @Test
    void getReceptionistProfileByUserId_deberiaRetornarPerfilRecepcionista() {
        when(receptionistProfileRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(receptionistProfile));

        ReceptionistProfileResponseDTO response = receptionistProfileService.getReceptionistProfileByUserId(1L);

        assertNotNull(response);
        assertEquals(1L, response.receptionistProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Mañana", response.shift());
        assertEquals("Admisión", response.department());

        verify(receptionistProfileRepository)
                .findByUserUserId(1L);
    }

    @Test
    void getReceptionistProfileByUserId_deberiaFallarCuandoNoExiste() {
        when(receptionistProfileRepository.findByUserUserId(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receptionistProfileService.getReceptionistProfileByUserId(99L));

        assertEquals(
                "Perfil de recepcionista no encontrado",
                exception.getMessage());
    }

    @Test
    void getAllReceptionistProfiles_deberiaRetornarLista() {
        when(receptionistProfileRepository.findAll())
                .thenReturn(List.of(receptionistProfile));

        List<ReceptionistProfileResponseDTO> response = receptionistProfileService.getAllReceptionistProfiles();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Mañana", response.get(0).shift());
        assertEquals("Admisión", response.get(0).department());

        verify(receptionistProfileRepository)
                .findAll();
    }

    @Test
    void getReceptionistProfileById_deberiaRetornarPerfilRecepcionista() {
        when(receptionistProfileRepository.findById(1L))
                .thenReturn(Optional.of(receptionistProfile));

        ReceptionistProfileResponseDTO response = receptionistProfileService.getReceptionistProfileById(1L);

        assertNotNull(response);
        assertEquals(1L, response.receptionistProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Mañana", response.shift());

        verify(receptionistProfileRepository)
                .findById(1L);
    }

    @Test
    void getReceptionistProfileById_deberiaFallarCuandoNoExiste() {
        when(receptionistProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receptionistProfileService.getReceptionistProfileById(99L));

        assertEquals(
                "Perfil de recepcionista no encontrado",
                exception.getMessage());
    }

    @Test
    void existsByUserId_deberiaRetornarTrue() {
        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        boolean response = receptionistProfileService.existsByUserId(1L);

        assertTrue(response);

        verify(receptionistProfileRepository)
                .existsByUserUserId(1L);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void updateReceptionistProfile_deberiaActualizarPerfilRecepcionista() {
        UpdateReceptionistProfileRequestDTO request = new UpdateReceptionistProfileRequestDTO(
                "Tarde",
                "Atención al paciente");

        when(receptionistProfileRepository.findById(1L))
                .thenReturn(Optional.of(receptionistProfile));

        when(receptionistProfileRepository.save(receptionistProfile))
                .thenReturn(receptionistProfile);

        ReceptionistProfileResponseDTO response = receptionistProfileService.updateReceptionistProfile(1L, request);

        assertNotNull(response);
        assertEquals("Tarde", response.shift());
        assertEquals("Atención al paciente", response.department());

        verify(receptionistProfileRepository)
                .save(receptionistProfile);
    }

    @Test
    void updateReceptionistProfile_deberiaFallarCuandoNoExiste() {
        UpdateReceptionistProfileRequestDTO request = new UpdateReceptionistProfileRequestDTO(
                "Tarde",
                "Atención al paciente");

        when(receptionistProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receptionistProfileService.updateReceptionistProfile(99L, request));

        assertEquals(
                "Perfil de recepcionista no encontrado",
                exception.getMessage());

        verify(receptionistProfileRepository, never())
                .save(any(ReceptionistProfile.class));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void deleteReceptionistProfile_deberiaEliminarPerfilRecepcionista() {
        when(receptionistProfileRepository.findById(1L))
                .thenReturn(Optional.of(receptionistProfile));

        receptionistProfileService.deleteReceptionistProfile(1L);

        verify(receptionistProfileRepository)
                .delete(receptionistProfile);
    }

    @Test
    void deleteReceptionistProfile_deberiaFallarCuandoNoExiste() {
        when(receptionistProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receptionistProfileService.deleteReceptionistProfile(99L));

        assertEquals(
                "Perfil de recepcionista no encontrado",
                exception.getMessage());

        verify(receptionistProfileRepository, never())
                .delete(any(ReceptionistProfile.class));
    }
}