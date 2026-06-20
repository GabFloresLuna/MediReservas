package cl.duoc.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import cl.duoc.users.dto.CreateUserProfileRequestDTO;
import cl.duoc.users.dto.UpdateUserProfileRequestDTO;
import cl.duoc.users.dto.UserProfileResponseDTO;
import cl.duoc.users.model.User;
import cl.duoc.users.model.UserProfile;
import cl.duoc.users.repository.AdministratorProfileRepository;
import cl.duoc.users.repository.PatientProfileRepository;
import cl.duoc.users.repository.ReceptionistProfileRepository;
import cl.duoc.users.repository.UserProfileRepository;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserService userService;

    @Mock
    private PatientProfileRepository patientProfileRepository;

    @Mock
    private ReceptionistProfileRepository receptionistProfileRepository;

    @Mock
    private AdministratorProfileRepository administratorProfileRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private User user;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setAuthUserId(10L);
        user.setRun("12345678-9");
        user.setEmail("paciente@test.cl");
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.of(2026, 6, 18, 12, 0));

        userProfile = new UserProfile();
        userProfile.setUserProfileId(1L);
        userProfile.setUser(user);
        userProfile.setFirstName("Juan");
        userProfile.setLastName("Pérez");
        userProfile.setPhone("912345678");
        userProfile.setBirthDate(LocalDate.of(2000, 1, 15));
        userProfile.setAddress("Av. Siempre Viva 123");
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Test
    void createUserProfile_deberiaCrearPerfilGeneralCorrectamente() {
        CreateUserProfileRequestDTO request = new CreateUserProfileRequestDTO(
                1L,
                "Juan",
                "Pérez",
                "912345678",
                LocalDate.of(2000, 1, 15),
                "Av. Siempre Viva 123");

        when(userProfileRepository.existsByUserUserId(request.userId()))
                .thenReturn(false);

        when(userService.findUserEntityById(request.userId()))
                .thenReturn(user);

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> {
                    UserProfile saved = invocation.getArgument(0);
                    saved.setUserProfileId(1L);
                    return saved;
                });

        UserProfileResponseDTO response = userProfileService.createUserProfile(request);

        assertNotNull(response);
        assertEquals(1L, response.userProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Juan", response.firstName());
        assertEquals("Pérez", response.lastName());
        assertEquals("912345678", response.phone());
        assertEquals(LocalDate.of(2000, 1, 15), response.birthDate());
        assertEquals("Av. Siempre Viva 123", response.address());

        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);

        verify(userProfileRepository).save(captor.capture());

        UserProfile savedProfile = captor.getValue();

        assertEquals(user, savedProfile.getUser());
        assertEquals("Juan", savedProfile.getFirstName());
        assertEquals("Pérez", savedProfile.getLastName());
        assertEquals("912345678", savedProfile.getPhone());
        assertEquals(LocalDate.of(2000, 1, 15), savedProfile.getBirthDate());
        assertEquals("Av. Siempre Viva 123", savedProfile.getAddress());
    }

    @Test
    void createUserProfile_deberiaFallarCuandoUsuarioYaTienePerfilGeneral() {
        CreateUserProfileRequestDTO request = new CreateUserProfileRequestDTO(
                1L,
                "Juan",
                "Pérez",
                "912345678",
                LocalDate.of(2000, 1, 15),
                "Av. Siempre Viva 123");

        when(userProfileRepository.existsByUserUserId(request.userId()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.createUserProfile(request));

        assertEquals(
                "El usuario ya tiene un perfil general",
                exception.getMessage());

        verify(userService, never())
                .findUserEntityById(any());

        verify(userProfileRepository, never())
                .save(any(UserProfile.class));
    }

    // =========================================================
    // GET
    // =========================================================

    @Test
    void getProfileByUserId_deberiaRetornarPerfilGeneral() {
        when(userProfileRepository.findByUserUserId(1L))
                .thenReturn(Optional.of(userProfile));

        UserProfileResponseDTO response = userProfileService.getProfileByUserId(1L);

        assertNotNull(response);
        assertEquals(1L, response.userProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Juan", response.firstName());

        verify(userProfileRepository)
                .findByUserUserId(1L);
    }

    @Test
    void getProfileByUserId_deberiaFallarCuandoNoExiste() {
        when(userProfileRepository.findByUserUserId(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.getProfileByUserId(99L));

        assertEquals(
                "Perfil general no encontrado",
                exception.getMessage());
    }

    @Test
    void getAllProfiles_deberiaRetornarLista() {
        when(userProfileRepository.findAll())
                .thenReturn(List.of(userProfile));

        List<UserProfileResponseDTO> response = userProfileService.getAllProfiles();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Juan", response.get(0).firstName());
        assertEquals("Pérez", response.get(0).lastName());

        verify(userProfileRepository).findAll();
    }

    @Test
    void getProfileById_deberiaRetornarPerfilGeneral() {
        when(userProfileRepository.findById(1L))
                .thenReturn(Optional.of(userProfile));

        UserProfileResponseDTO response = userProfileService.getProfileById(1L);

        assertNotNull(response);
        assertEquals(1L, response.userProfileId());
        assertEquals(1L, response.userId());
        assertEquals("Juan", response.firstName());

        verify(userProfileRepository).findById(1L);
    }

    @Test
    void getProfileById_deberiaFallarCuandoNoExiste() {
        when(userProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.getProfileById(99L));

        assertEquals(
                "Perfil general no encontrado",
                exception.getMessage());
    }

    @Test
    void existsByUserId_deberiaRetornarTrue() {
        when(userProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        boolean response = userProfileService.existsByUserId(1L);

        assertTrue(response);

        verify(userProfileRepository)
                .existsByUserUserId(1L);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void updateUserProfile_deberiaActualizarPerfilGeneral() {
        UpdateUserProfileRequestDTO request = new UpdateUserProfileRequestDTO(
                "Pedro",
                "González",
                "987654321",
                LocalDate.of(1998, 5, 20),
                "Nueva dirección 456");

        when(userProfileRepository.findById(1L))
                .thenReturn(Optional.of(userProfile));

        when(userProfileRepository.save(userProfile))
                .thenReturn(userProfile);

        UserProfileResponseDTO response = userProfileService.updateUserProfile(1L, request);

        assertNotNull(response);
        assertEquals("Pedro", response.firstName());
        assertEquals("González", response.lastName());
        assertEquals("987654321", response.phone());
        assertEquals(LocalDate.of(1998, 5, 20), response.birthDate());
        assertEquals("Nueva dirección 456", response.address());

        verify(userProfileRepository).save(userProfile);
    }

    @Test
    void updateUserProfile_deberiaFallarCuandoNoExiste() {
        UpdateUserProfileRequestDTO request = new UpdateUserProfileRequestDTO(
                "Pedro",
                "González",
                "987654321",
                LocalDate.of(1998, 5, 20),
                "Nueva dirección 456");

        when(userProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.updateUserProfile(99L, request));

        assertEquals(
                "Perfil general no encontrado",
                exception.getMessage());

        verify(userProfileRepository, never())
                .save(any(UserProfile.class));
    }

    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void deleteUserProfile_deberiaEliminarPerfilGeneralCuandoNoTienePerfilEspecifico() {
        when(userProfileRepository.findById(1L))
                .thenReturn(Optional.of(userProfile));

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        userProfileService.deleteUserProfile(1L);

        verify(userProfileRepository).delete(userProfile);
    }

    @Test
    void deleteUserProfile_deberiaFallarCuandoTienePerfilPaciente() {
        when(userProfileRepository.findById(1L))
                .thenReturn(Optional.of(userProfile));

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.deleteUserProfile(1L));

        assertEquals(
                "No se puede eliminar el perfil general porque el usuario tiene un perfil de paciente",
                exception.getMessage());

        verify(userProfileRepository, never())
                .delete(any(UserProfile.class));
    }

    @Test
    void deleteUserProfile_deberiaFallarCuandoTienePerfilRecepcionista() {
        when(userProfileRepository.findById(1L))
                .thenReturn(Optional.of(userProfile));

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.deleteUserProfile(1L));

        assertEquals(
                "No se puede eliminar el perfil general porque el usuario tiene un perfil de recepcionista",
                exception.getMessage());

        verify(userProfileRepository, never())
                .delete(any(UserProfile.class));
    }

    @Test
    void deleteUserProfile_deberiaFallarCuandoTienePerfilAdministrador() {
        when(userProfileRepository.findById(1L))
                .thenReturn(Optional.of(userProfile));

        when(patientProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(receptionistProfileRepository.existsByUserUserId(1L))
                .thenReturn(false);

        when(administratorProfileRepository.existsByUserUserId(1L))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.deleteUserProfile(1L));

        assertEquals(
                "No se puede eliminar el perfil general porque el usuario tiene un perfil de administrador",
                exception.getMessage());

        verify(userProfileRepository, never())
                .delete(any(UserProfile.class));
    }

    @Test
    void deleteUserProfile_deberiaFallarCuandoPerfilNoExiste() {
        when(userProfileRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userProfileService.deleteUserProfile(99L));

        assertEquals(
                "Perfil general no encontrado",
                exception.getMessage());

        verify(userProfileRepository, never())
                .delete(any(UserProfile.class));
    }
}