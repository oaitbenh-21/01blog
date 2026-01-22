package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.Dto.AnalyticsDto;
import blog.Dto.ReportDto;
import blog.Dto.UserDto;
import blog.Model.Post;
import blog.Model.User;
import blog.Services.AdminService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(adminService.getAllPosts());
    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDto>> getAllReports() {
        return ResponseEntity.ok(adminService.getAllReports());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}/ban")
    public ResponseEntity<String> banUser(@PathVariable Long id) {
        adminService.banUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        adminService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reports/{id}")
    public ResponseEntity<Void> resolveReport(@PathVariable Long id) {
        adminService.resolveReport(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> removeReport(@PathVariable Long id) {
        adminService.removeReport(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsDto> getAnalytics() {
        return ResponseEntity.ok(adminService.getAnalytics());
    }
}
