package blog.user.app.report.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.user.app.report.enums.ReportStatus;

public interface ReportRepository extends JpaRepository<ReportEntity, UUID> {

    // Prevent duplicate reports
    boolean existsByReporter_IdAndReported_Id(UUID reporterId, UUID reportedId);

    // Admin: view all reports
    List<ReportEntity> findAllByOrderByCreatedAtDesc();

    // Admin: view reports against a user
    List<ReportEntity> findByReported_IdOrderByCreatedAtDesc(UUID reportedId);

    // Admin: count reports against a user
    long countByReported_Id(UUID reportedId);

    void updateReportStatus(UUID reportId, ReportStatus status);

}
