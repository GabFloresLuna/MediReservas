package cl.duoc.prescriptions.controller;

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

import cl.duoc.prescriptions.dto.PrescriptionItemRequest;
import cl.duoc.prescriptions.dto.PrescriptionItemResponse;
import cl.duoc.prescriptions.services.PrescriptionItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/prescription-items")
@RequiredArgsConstructor
public class PrescriptionItemController {

    private final PrescriptionItemService prescriptionItemService;

    @PostMapping
    public ResponseEntity<PrescriptionItemResponse> create(@Valid @RequestBody PrescriptionItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionItemService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionItemResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionItemService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionItemResponse>> findAll() {
        return ResponseEntity.ok(prescriptionItemService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionItemResponse> update(@PathVariable Long id, @Valid @RequestBody PrescriptionItemRequest request) {
        return ResponseEntity.ok(prescriptionItemService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prescriptionItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

}