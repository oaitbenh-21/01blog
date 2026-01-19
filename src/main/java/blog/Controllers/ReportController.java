package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.Dto.ReportRequest;
import blog.Services.ReportService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<Void> submitReport(@RequestBody @Valid ReportRequest reportDto) {
        reportService.submitReport(reportDto);
        return ResponseEntity.ok().build();
    }

}
