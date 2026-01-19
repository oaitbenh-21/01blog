package blog.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import blog.Dto.AnalyticsDto;
import blog.Dto.ReportDto;
import blog.Model.Post;
import blog.Model.User;
import blog.Repositories.CommentRepository;
import blog.Repositories.PostRepository;
import blog.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReportService reportService;
    @Autowired
    private CommentRepository commentRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<ReportDto> getAllReports() {
        return reportService.getAllReports();
    }

    public void banUser(Long userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setBanned(true);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (userService.getCurrentUser().getId().equals(id)) {
            throw new RuntimeException("admin cannot delete themselves");
        }
        userRepository.deleteById(id);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public void resolveReport(Long id) {
        reportService.resolveReport(id);
    }

    public void deleteComment(Long id) {
        commentRepository
                .delete(commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found")));
    }

    public AnalyticsDto getAnalytics() {
        AnalyticsDto analytic = new AnalyticsDto();
        if (this.getAllReports() != null) {
            analytic.setTotalReports(this.getAllReports().size());
        }
        if (this.getAllPosts() != null) {
            analytic.setTotalPosts(this.getAllPosts().size());
        }
        if (this.getAllUsers() != null) {
            analytic.setTotalUsers(this.getAllUsers().size());
        }
        return analytic;
    }
}
