package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.Dto.ReportDto;
import blog.Model.Report;
import blog.Model.enums.ReportStatus;
import blog.Repositories.ReportRepository;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserService userService;

    public void submitReport(ReportDto dto) {
        Report report = new Report();
        report.setReportedBy(userService.getCurrentUser());
        report.setReportedUser(userService.getUserById(dto.getReportedUserId()));
        report.setReason(dto.getReason());
        reportRepository.save(report);
    }

    public List<ReportDto> getAllReports() {
        return reportRepository.findByStatus(ReportStatus.PENDING)
                .stream().map(r -> {
                    ReportDto dto = new ReportDto();
                    dto.setId(r.getId());
                    dto.setReporterId(r.getReportedBy().getId());
                    dto.setReportedUserId(r.getReportedUser().getId());
                    dto.setReason(r.getReason());
                    dto.setCreatedAt(r.getCreatedAt());
                    dto.setStatus(r.getStatus().name());
                    return dto;
                }).toList();
    }

    public void resolveReport(Long id, String action) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(ReportStatus.RESOLVED);
        reportRepository.save(report);
        // Action could be ban user or delete post; handle externally
    }
}
