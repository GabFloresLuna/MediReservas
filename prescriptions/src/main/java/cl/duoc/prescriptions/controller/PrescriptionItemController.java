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

import cl.duoc.prescriptions.dto.ApiResponse;
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
    public ResponseEntity<ApiResponse<PrescriptionItemResponse>> create(@Valid @RequestBody PrescriptionItemRequest request) {
        PrescriptionItemResponse data = prescriptionItemService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(201, "Ítem creado", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionItemResponse>> findById(@PathVariable Long id) {
        PrescriptionItemResponse data = prescriptionItemService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Ítem encontrado", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PrescriptionItemResponse>>> findAll() {
        List<PrescriptionItemResponse> data = prescriptionItemService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lista de ítems", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescriptionItemResponse>> update(@PathVariable Long id, @Valid @RequestBody PrescriptionItemRequest request) {
        PrescriptionItemResponse data = prescriptionItemService.update(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Ítem actualizado", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        prescriptionItemService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Ítem eliminado", null));
    }
}