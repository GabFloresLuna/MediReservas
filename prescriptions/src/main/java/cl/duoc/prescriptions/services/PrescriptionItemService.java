package cl.duoc.prescriptions.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.prescriptions.dto.PrescriptionItemRequest;
import cl.duoc.prescriptions.dto.PrescriptionItemResponse;
import cl.duoc.prescriptions.model.Prescription;
import cl.duoc.prescriptions.model.PrescriptionItem;
import cl.duoc.prescriptions.repository.PrescriptionItemRepository;
import cl.duoc.prescriptions.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescriptionItemService {

    private final PrescriptionItemRepository prescriptionItemRepository;
    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionItemResponse create(PrescriptionItemRequest request) {
        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("Prescripción no encontrada con ID: " + request.getPrescriptionId()));

        PrescriptionItem item = new PrescriptionItem();
        item.setPrescription(prescription);
        item.setMedicineName(request.getMedicineName());
        item.setDosage(request.getDosage());
        item.setFrequency(request.getFrequency());
        item.setDuration(request.getDuration());
        item.setInstructions(request.getInstructions());

        PrescriptionItem saved = prescriptionItemRepository.save(item);
        return mapToResponse(saved);
    }

    public PrescriptionItemResponse findById(Long id) {
        PrescriptionItem item = prescriptionItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ítem de prescripción no encontrado con ID: " + id));
        return mapToResponse(item);
    }

    public List<PrescriptionItemResponse> findAll() {
        return prescriptionItemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PrescriptionItemResponse update(Long id, PrescriptionItemRequest request) {
        PrescriptionItem item = prescriptionItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ítem de prescripción no encontrado con ID: " + id));

        Prescription prescription = prescriptionRepository.findById(request.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("Prescripción no encontrada con ID: " + request.getPrescriptionId()));

        item.setPrescription(prescription);
        item.setMedicineName(request.getMedicineName());
        item.setDosage(request.getDosage());
        item.setFrequency(request.getFrequency());
        item.setDuration(request.getDuration());
        item.setInstructions(request.getInstructions());

        PrescriptionItem updated = prescriptionItemRepository.save(item);
        return mapToResponse(updated);
    }

    public void delete(Long id) {
        if (!prescriptionItemRepository.existsById(id)) {
            throw new RuntimeException("Ítem de prescripción no encontrado con ID: " + id);
        }
        prescriptionItemRepository.deleteById(id);
    }

    private PrescriptionItemResponse mapToResponse(PrescriptionItem item) {
        return new PrescriptionItemResponse(
                item.getPrescriptionItemId(),
                item.getPrescription().getPrescriptionId(),
                item.getMedicineName(),
                item.getDosage(),
                item.getFrequency(),
                item.getDuration(),
                item.getInstructions()
        );
    }
}