package cl.duoc.reports.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.reports.model.GeneratedReports;

@Repository
public interface GeneratedReportsRepository extends JpaRepository<GeneratedReports, Long>{

}
