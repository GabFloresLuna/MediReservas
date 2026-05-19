package cl.duoc.doctors.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.doctors.dto.DoctorsDTO;
import cl.duoc.doctors.service.DoctorsService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorsController {
    
    private final DoctorsService doctorsService;

    public DoctorsController(DoctorsService doctorsService) {
        this.doctorsService = doctorsService;
    }

    // GET: Obtener todos los doctores
    @GetMapping
    public ResponseEntity<List<DoctorsDTO>> getAllDoctors() {
        List<DoctorsDTO> doctors = doctorsService.findAll();
        return ResponseEntity.ok(doctors);
    }

    // GET: Obtener un doctor por ID
    @GetMapping("/{id}")
    public ResponseEntity<DoctorsDTO> getDoctorById(@PathVariable Long id) {
        DoctorsDTO doctor = doctorsService.findById(id);
        return ResponseEntity.ok(doctor);
    }
    // POST: Crear un nuevo doctor
    @PostMapping
    public ResponseEntity<DoctorsDTO> createDoctor(@RequestBody DoctorsDTO doctorsDTO) {
        DoctorsDTO createdDoctor = doctorsService.save(doctorsDTO);
        return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
    }

    // PUT: Actualizar un doctor existente
    @PutMapping("/{id}")
    public ResponseEntity<DoctorsDTO> updateDoctor(@PathVariable Long id, @RequestBody DoctorsDTO doctorsDTO) {
        DoctorsDTO updatedDoctor = doctorsService.update(id, doctorsDTO);
        return ResponseEntity.ok(updatedDoctor);
    }

    // DELETE: Eliminar (desactivar) un doctor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}