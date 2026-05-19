package cl.duoc.prescriptions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.prescriptions.model.PrescriptionItem;

@Repository
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {}