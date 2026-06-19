package cl.duoc.prescriptions.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.prescriptions.dto.PrescriptionItemRequest;
import cl.duoc.prescriptions.dto.PrescriptionItemResponse;
import cl.duoc.prescriptions.dto.PrescriptionResponse;
import cl.duoc.prescriptions.model.Prescription;
import cl.duoc.prescriptions.model.PrescriptionItem;
import cl.duoc.prescriptions.repository.PrescriptionItemRepository;
import cl.duoc.prescriptions.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        try {
            PrescriptionItem item = prescriptionItemRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item de prescripción no encontrado con ID: " + id));
            return mapToResponse(item);
        } catch (RuntimeException ex) {
            log.error("Error finding PrescriptionItem by id: {}", id, ex);
            throw ex;
        }
    }

    public List<PrescriptionItemResponse> findAll() {
        return prescriptionItemRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PrescriptionItemResponse update(Long id, PrescriptionItemRequest request) {
        try {
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
        } catch (RuntimeException ex) {
            log.error("Error updating PrescriptionItem with id: {}", id, ex);
            throw ex;
        }
    }

    public void delete(Long id) {
        try {
            if (!prescriptionItemRepository.existsById(id)) {
                throw new RuntimeException("Item de prescripción no encontrado con ID: " + id);
            }
            prescriptionItemRepository.deleteById(id);
        } catch (RuntimeException ex) {
            log.error("Error deleting PrescriptionItem with id: {}", id, ex);
            throw ex;
        }
    }

    private PrescriptionItemResponse mapToResponse(PrescriptionItem item) {
        PrescriptionResponse prescriptionResponse = new PrescriptionResponse(
                item.getPrescription().getPrescriptionId(),
                item.getPrescription().getMedicalVisitId(),
                item.getPrescription().getPatientUserId(),
                item.getPrescription().getDoctorId(),
                item.getPrescription().getIssuedAt(),
                item.getPrescription().getPrescriptionStatus().name(),
                item.getPrescription().getNotes()
        );

        return new PrescriptionItemResponse(
                item.getPrescriptionItemId(),
                prescriptionResponse,
                item.getMedicineName(),
                item.getDosage(),
                item.getFrequency(),
                item.getDuration(),
                item.getInstructions()
        );
    }
}