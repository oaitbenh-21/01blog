package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.Dto.ReportDto;
import blog.Services.ReportService;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // Submit a report
    @PostMapping
    public ResponseEntity<String> submitReport(@RequestBody ReportDto reportDto) {
        reportService.submitReport(reportDto);
        return ResponseEntity.ok("Report submitted.");
    }

    // Admin: get all reports
    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    // Admin: resolve a report
    @PostMapping("/{id}/action")
    public ResponseEntity<String> resolveReport(@PathVariable Long id, @RequestBody String action) {
        reportService.resolveReport(id, action);
        return ResponseEntity.ok("Report resolved.");
    }
}
