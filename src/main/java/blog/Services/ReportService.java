package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.Dto.ReportDto;
import blog.Dto.ReportRequest;
import blog.Model.Post;
import blog.Model.Report;
import blog.Model.User;
import blog.Model.enums.ReportStatus;
import blog.Repositories.ReportRepository;

import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    public void reportUser(ReportRequest dto) {
        if (dto.getUserid() == 0)
            throw new RuntimeException("you should to add user id");
        User user = userService.getCurrentUser();
        if (user.getId().equals(dto.getUserid())) {
            throw new RuntimeException("Cannot report yourself");
        }
        Report report = new Report();
        report.setReporter(user);
        report.setUser(userService.getUserById(dto.getUserid()));
        report.setReason(dto.getReason());
        report.setStatus(ReportStatus.PENDING);
        reportRepository.save(report);
    }

    public void reportPost(ReportRequest dto) {
        if (dto.getPostid() == 0)
            throw new RuntimeException("you should to add post id");
        User user = userService.getCurrentUser();
        Report report = new Report();
        report.setReporter(user);
        report.setPost(postService.getPostById(dto.getPostid()));
        report.setReason(dto.getReason());
        report.setStatus(ReportStatus.PENDING);
        reportRepository.save(report);
    }

    public List<ReportDto> getAllReports() {
        return reportRepository.findAll()
                .stream().map(r -> {
                    Post post = r.getPost();
                    Long post_id = post == null ? 0 : post.getId();
                    User user = r.getUser();
                    Long user_id = user == null ? 0 : user.getId();
                    ReportDto dto = new ReportDto();
                    dto.setId(r.getId());
                    dto.setReporterId(r.getReporter().getId());
                    dto.setUserId(user_id);
                    dto.setPostId(post_id);
                    dto.setReason(r.getReason());
                    dto.setCreatedAt(r.getCreatedAt());
                    dto.setStatus(r.getStatus().name());
                    return dto;
                }).toList();
    }

    public void resolveReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(ReportStatus.RESOLVED);
        reportRepository.save(report);
        // action could be ban user or delete post; handle externally
    }
}
