package cl.duoc.specialties.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.specialties.dto.CreateSpecialtyRequestDTO;
import cl.duoc.specialties.dto.SpecialtyResponseDTO;
import cl.duoc.specialties.model.Specialty;
import cl.duoc.specialties.repository.SpecialtyRepository;

@ExtendWith(MockitoExtension.class)
class SpecialtyServiceTest {

    @Mock
    private SpecialtyRepository specialtyRepository;

    @InjectMocks
    private SpecialtyService specialtyService;

    private Specialty specialty;

    @BeforeEach
    void setUp() {
        specialty = new Specialty();
        specialty.setSpecialtyId(1L);
        specialty.setSpecialtyName("Cardiología");
        specialty.setDescription("Especialidad médica del corazón");
        specialty.setActive(true);
        specialty.setCreatedAt(LocalDateTime.of(2026, 6, 18, 12, 0));
    }

    // =========================================================
    // CREATE
    // =========================================================

    @Test
    void createSpecialty_deberiaCrearEspecialidadCorrectamente() {
        CreateSpecialtyRequestDTO request = new CreateSpecialtyRequestDTO(
                "Cardiología",
                "Especialidad médica del corazón");

        when(specialtyRepository.existsBySpecialtyName(request.specialtyName()))
                .thenReturn(false);

        when(specialtyRepository.save(any(Specialty.class)))
                .thenAnswer(invocation -> {
                    Specialty saved = invocation.getArgument(0);
                    saved.setSpecialtyId(1L);
                    saved.setCreatedAt(LocalDateTime.of(2026, 6, 18, 12, 0));
                    return saved;
                });

        SpecialtyResponseDTO response = specialtyService.createSpecialty(request);

        assertNotNull(response);
        assertEquals(1L, response.specialtyId());
        assertEquals("Cardiología", response.specialtyName());
        assertEquals("Especialidad médica del corazón", response.description());
        assertTrue(response.active());

        ArgumentCaptor<Specialty> captor = ArgumentCaptor.forClass(Specialty.class);

        verify(specialtyRepository).save(captor.capture());

        Specialty savedSpecialty = captor.getValue();

        assertEquals("Cardiología", savedSpecialty.getSpecialtyName());
        assertEquals("Especialidad médica del corazón", savedSpecialty.getDescription());
        assertTrue(savedSpecialty.isActive());
    }

    @Test
    void createSpecialty_deberiaFallarCuandoNombreYaExiste() {
        CreateSpecialtyRequestDTO request = new CreateSpecialtyRequestDTO(
                "Cardiología",
                "Especialidad médica del corazón");

        when(specialtyRepository.existsBySpecialtyName(request.specialtyName()))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> specialtyService.createSpecialty(request));

        assertEquals(
                "Ya existe una especialidad con ese nombre",
                exception.getMessage());

        verify(specialtyRepository, never())
                .save(any(Specialty.class));
    }

    // =========================================================
    // GET
    // =========================================================

    @Test
    void getAllSpecialties_deberiaRetornarLista() {
        when(specialtyRepository.findAll())
                .thenReturn(List.of(specialty));

        List<SpecialtyResponseDTO> response = specialtyService.getAllSpecialties();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Cardiología", response.get(0).specialtyName());

        verify(specialtyRepository).findAll();
    }

    @Test
    void getSpecialtyById_deberiaRetornarEspecialidad() {
        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        SpecialtyResponseDTO response = specialtyService.getSpecialtyById(1L);

        assertNotNull(response);
        assertEquals(1L, response.specialtyId());
        assertEquals("Cardiología", response.specialtyName());
        assertTrue(response.active());

        verify(specialtyRepository).findById(1L);
    }

    @Test
    void getSpecialtyById_deberiaFallarCuandoNoExiste() {
        when(specialtyRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> specialtyService.getSpecialtyById(99L));

        assertEquals(
                "Especialidad no encontrada",
                exception.getMessage());
    }

    @Test
    void getSpecialtyByName_deberiaRetornarEspecialidad() {
        when(specialtyRepository.findBySpecialtyName("Cardiología"))
                .thenReturn(Optional.of(specialty));

        SpecialtyResponseDTO response = specialtyService.getSpecialtyByName("Cardiología");

        assertNotNull(response);
        assertEquals("Cardiología", response.specialtyName());
        assertEquals("Especialidad médica del corazón", response.description());

        verify(specialtyRepository).findBySpecialtyName("Cardiología");
    }

    @Test
    void getSpecialtyByName_deberiaFallarCuandoNoExiste() {
        when(specialtyRepository.findBySpecialtyName("Inventada"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> specialtyService.getSpecialtyByName("Inventada"));

        assertEquals(
                "Especialidad no encontrada",
                exception.getMessage());
    }

    // =========================================================
    // EXISTS
    // =========================================================

    @Test
    void existsById_deberiaRetornarTrue() {
        when(specialtyRepository.existsById(1L))
                .thenReturn(true);

        boolean response = specialtyService.existsById(1L);

        assertTrue(response);

        verify(specialtyRepository).existsById(1L);
    }

    @Test
    void existsActiveById_deberiaRetornarTrue() {
        when(specialtyRepository.existsBySpecialtyIdAndActiveTrue(1L))
                .thenReturn(true);

        boolean response = specialtyService.existsActiveById(1L);

        assertTrue(response);

        verify(specialtyRepository)
                .existsBySpecialtyIdAndActiveTrue(1L);
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void updateSpecialty_deberiaActualizarEspecialidad() {
        CreateSpecialtyRequestDTO request = new CreateSpecialtyRequestDTO(
                "Medicina General",
                "Atención médica general");

        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        when(specialtyRepository.existsBySpecialtyName("Medicina General"))
                .thenReturn(false);

        when(specialtyRepository.save(specialty))
                .thenReturn(specialty);

        SpecialtyResponseDTO response = specialtyService.updateSpecialty(1L, request);

        assertNotNull(response);
        assertEquals("Medicina General", response.specialtyName());
        assertEquals("Atención médica general", response.description());

        verify(specialtyRepository).save(specialty);
    }

    @Test
    void updateSpecialty_deberiaPermitirMismoNombreConDistintaDescripcion() {
        CreateSpecialtyRequestDTO request = new CreateSpecialtyRequestDTO(
                "cardiología",
                "Descripción actualizada");

        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        when(specialtyRepository.save(specialty))
                .thenReturn(specialty);

        SpecialtyResponseDTO response = specialtyService.updateSpecialty(1L, request);

        assertNotNull(response);
        assertEquals("cardiología", response.specialtyName());
        assertEquals("Descripción actualizada", response.description());

        verify(specialtyRepository, never())
                .existsBySpecialtyName(any());

        verify(specialtyRepository).save(specialty);
    }

    @Test
    void updateSpecialty_deberiaFallarCuandoNuevoNombreYaExiste() {
        CreateSpecialtyRequestDTO request = new CreateSpecialtyRequestDTO(
                "Neurología",
                "Especialidad del sistema nervioso");

        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        when(specialtyRepository.existsBySpecialtyName("Neurología"))
                .thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> specialtyService.updateSpecialty(1L, request));

        assertEquals(
                "Ya existe una especialidad con ese nombre",
                exception.getMessage());

        verify(specialtyRepository, never())
                .save(any(Specialty.class));
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Test
    void deactivateSpecialty_deberiaDesactivarEspecialidad() {
        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        when(specialtyRepository.save(specialty))
                .thenReturn(specialty);

        SpecialtyResponseDTO response = specialtyService.deactivateSpecialty(1L);

        assertNotNull(response);
        assertFalse(response.active());
        assertFalse(specialty.isActive());

        verify(specialtyRepository).save(specialty);
    }

    @Test
    void deactivateSpecialty_deberiaFallarCuandoYaEstaDesactivada() {
        specialty.setActive(false);

        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> specialtyService.deactivateSpecialty(1L));

        assertEquals(
                "La especialidad ya está desactivada",
                exception.getMessage());

        verify(specialtyRepository, never())
                .save(any(Specialty.class));
    }

    // =========================================================
    // ACTIVATE
    // =========================================================

    @Test
    void activateSpecialty_deberiaActivarEspecialidad() {
        specialty.setActive(false);

        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        when(specialtyRepository.save(specialty))
                .thenReturn(specialty);

        SpecialtyResponseDTO response = specialtyService.activateSpecialty(1L);

        assertNotNull(response);
        assertTrue(response.active());
        assertTrue(specialty.isActive());

        verify(specialtyRepository).save(specialty);
    }

    @Test
    void activateSpecialty_deberiaFallarCuandoYaEstaActivada() {
        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> specialtyService.activateSpecialty(1L));

        assertEquals(
                "La especialidad ya está activada",
                exception.getMessage());

        verify(specialtyRepository, never())
                .save(any(Specialty.class));
    }
}