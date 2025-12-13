package blog.user.app.report;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import blog.user.app.report.enums.ReportStatus;
import blog.user.app.report.jpa.ReportEntity;
import blog.user.app.report.service.ReportService;
import blog.user.jpa.UserEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // * USER: Report another user
    @PostMapping("/{reportedId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> reportUser(
            @PathVariable UUID reportedId,
            @RequestParam String reason,
            @AuthenticationPrincipal UserEntity user) {
        try {
            reportService.reportUser(user.getId(), reportedId, reason);
            reportService.reportUser(user.getId(), reportedId, reason);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User reported successfully");
    }

    // * Update report status
    @PatchMapping("/{reportId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateStatus(
            @PathVariable UUID reportId,
            @RequestParam ReportStatus status) {
        try {
            reportService.updateReportStatus(reportId, status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok("Report status updated successfully");
    }

    // * ADMIN: Get all reports
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportEntity>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    // * ADMIN: Get reports against a user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportEntity>> getReportsByUser(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(reportService.getReportsByUser(userId));
    }

    // * ADMIN: Get report count for a user
    @GetMapping("/user/{userId}/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getReportCount(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(reportService.getReportCount(userId));
    }
}
