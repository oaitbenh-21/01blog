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

    @PostMapping("/user")
    public ResponseEntity<Void> reportUser(@RequestBody @Valid ReportRequest reportDto) {
        reportService.reportUser(reportDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post")
    public ResponseEntity<Void> reportPost(@RequestBody @Valid ReportRequest reportDto) {
        reportService.reportPost(reportDto);
        return ResponseEntity.ok().build();
    }
}
