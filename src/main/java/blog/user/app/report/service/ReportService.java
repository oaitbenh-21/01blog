package blog.user.app.report.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import blog.user.app.report.enums.ReportStatus;
import blog.user.app.report.jpa.ReportEntity;
import blog.user.app.report.jpa.ReportRepository;
import blog.user.app.report.service.ReportService;
import blog.user.jpa.UserEntity;
import blog.user.jpa.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public void reportUser(UUID reporterId, UUID reportedId, String reason) {

        if (reporterId.equals(reportedId)) {
            throw new IllegalArgumentException("Users cannot report themselves");
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Report reason is required");
        }

        if (reportRepository.existsByReporter_IdAndReported_Id(reporterId, reportedId)) {
            throw new IllegalStateException("User already reported");
        }

        UserEntity reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new IllegalArgumentException("Reporter not found"));

        UserEntity reported = userRepository.findById(reportedId)
                .orElseThrow(() -> new IllegalArgumentException("Reported user not found"));

        ReportEntity report = new ReportEntity();
        report.setReporter(reporter);
        report.setReported(reported);
        report.setStatus(ReportStatus.WAITING);
        report.setReason(reason);
        report.setCreatedAt(LocalDate.now());
        reportRepository.save(report);
    }

    public void updateReportStatus(UUID reportId, ReportStatus status) {

        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        if (report.getStatus() == ReportStatus.RESOLVED
                || report.getStatus() == ReportStatus.REJECTED) {
            throw new IllegalStateException("Report already closed");
        }

        report.setStatus(status);
        reportRepository.save(report);
    }

    public List<ReportEntity> getAllReports() {
        return reportRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<ReportEntity> getReportsByUser(UUID userId) {
        return reportRepository.findByReported_IdOrderByCreatedAtDesc(userId);
    }

    public long getReportCount(UUID userId) {
        return reportRepository.countByReported_Id(userId);
    }
}
