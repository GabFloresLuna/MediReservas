package cl.duoc.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.reports.model.ReportRequest;

@Repository
public interface ReportRequestRepository extends JpaRepository<ReportRequest, Long>{

}
