package blog.user.app.report.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import blog.user.app.report.dto.ReportRequest;
import blog.user.app.report.dto.ReportResponse;

@Service
public class ReportService {
    public ReportResponse createReport(UUID user_id, ReportRequest report) {
        return null;
    }
}
