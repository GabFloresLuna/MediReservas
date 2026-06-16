package cl.duoc.prescriptions.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.prescriptions.dto.PrescriptionRequest;
import cl.duoc.prescriptions.dto.PrescriptionResponse;
import cl.duoc.prescriptions.model.Prescription;
import cl.duoc.prescriptions.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionResponse create(PrescriptionRequest request) {
        Prescription prescription = new Prescription();
        prescription.setMedicalVisitId(request.getMedicalVisitId());
        prescription.setPatientUserId(request.getPatientUserId());
        prescription.setDoctorId(request.getDoctorId());
        prescription.setPrescriptionStatus(request.getPrescriptionStatus());
        prescription.setNotes(request.getNotes());
        prescription.setIssuedAt(LocalDateTime.now());

        Prescription saved = prescriptionRepository.save(prescription);
        return mapToResponse(saved);
    }

    public PrescriptionResponse findById(Long id) {
        try {
            Prescription prescription = prescriptionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Prescripción no encontrada con ID: " + id));
            return mapToResponse(prescription);
        } catch (RuntimeException ex) {
            log.error("Error finding Prescription by id: {}", id, ex);
            throw ex;
        }
    }

    public List<PrescriptionResponse> findAll() {
        return prescriptionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PrescriptionResponse update(Long id, PrescriptionRequest request) {
        try {
            Prescription prescription = prescriptionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Prescripción no encontrada con ID: " + id));

            prescription.setMedicalVisitId(request.getMedicalVisitId());
            prescription.setPatientUserId(request.getPatientUserId());
            prescription.setDoctorId(request.getDoctorId());
            prescription.setPrescriptionStatus(request.getPrescriptionStatus());
            prescription.setNotes(request.getNotes());

            Prescription updated = prescriptionRepository.save(prescription);
            return mapToResponse(updated);
        } catch (RuntimeException ex) {
            log.error("Error updating Prescription with id: {}", id, ex);
            throw ex;
        }
    }

    public void delete(Long id) {
        try {
            if (!prescriptionRepository.existsById(id)) {
                throw new RuntimeException("Prescripción no encontrada con ID: " + id);
            }
            prescriptionRepository.deleteById(id);
        } catch (RuntimeException ex) {
            log.error("Error deleting Prescription with id: {}", id, ex);
            throw ex;
        }
    }

    private PrescriptionResponse mapToResponse(Prescription prescription) {
        return new PrescriptionResponse(
                prescription.getPrescriptionId(),
                prescription.getMedicalVisitId(),
                prescription.getPatientUserId(),
                prescription.getDoctorId(),
                prescription.getIssuedAt(),
                prescription.getPrescriptionStatus().name(),
                prescription.getNotes()
        );
    }
}