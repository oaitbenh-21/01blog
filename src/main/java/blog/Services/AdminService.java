package blog.Services;

import org.springframework.stereotype.Service;

import blog.Dto.ReportDto;
import blog.Model.Post;
import blog.Model.User;
import blog.Repositories.PostRepository;
import blog.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReportService reportService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<ReportDto> getAllReports() {
        return reportService.getAllReports();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public void resolveReport(Long id, String action) {
        reportService.resolveReport(id, action);
    }

    public Map<String, Object> getAnalytics() {
        return Map.of(
                "totalUsers", userRepository.count(),
                "totalPosts", postRepository.count());
    }
}
