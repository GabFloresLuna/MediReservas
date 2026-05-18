package cl.duoc.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.reports.model.GeneratedReport;

@Repository
public interface GeneratedReportRepository extends JpaRepository<GeneratedReport, Long>{

}
