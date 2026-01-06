package blog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.Model.Report;
import blog.Model.enums.ReportStatus;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByStatus(ReportStatus status);
}
