package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import blog.Dto.ReportDto;
import blog.Dto.UserDto;
import blog.Model.Post;
import blog.Model.User;
import blog.Services.AdminService;

import java.util.List;
import java.util.Map;
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
        return ResponseEntity.ok("User deleted successfully.");
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        adminService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully.");
    }

        @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        adminService.deleteComment(id);
        return ResponseEntity.ok("Post deleted successfully.");
    }


    @PostMapping("/reports/{id}/resolve")
    public ResponseEntity<String> resolveReport(@PathVariable Long id, @RequestBody String action) {
        adminService.resolveReport(id, action);
        return ResponseEntity.ok("Report resolved.");
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        return ResponseEntity.ok(adminService.getAnalytics());
    }
}
