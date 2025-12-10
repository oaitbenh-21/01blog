package blog.user.app.report;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import blog.user.app.report.dto.ReportRequest;
import blog.user.app.report.dto.ReportResponse;
import blog.user.app.report.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReportResponse> createReport(
            @AuthenticationPrincipal UUID userId,
            @Valid @RequestBody ReportRequest createReportDto) {
        ReportResponse report = reportService.createReport(userId, createReportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

}
