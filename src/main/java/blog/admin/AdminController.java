package blog.admin;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    /* ============================
       USER MANAGEMENT
       ============================ */

    // View all users
    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() {
        return ResponseEntity.ok("list of users");
    }

    // View a single user
    @GetMapping("/user/{uuid}")
    public ResponseEntity<String> getUser(@PathVariable UUID uuid) {
        return ResponseEntity.ok("user details: " + uuid);
    }

    // Delete a user
    @DeleteMapping("/user/{uuid}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuid) {
        return ResponseEntity.ok("user deleted: " + uuid);
    }

    // Ban a user
    @PostMapping("/user/ban/{uuid}")
    public ResponseEntity<String> banUser(@PathVariable UUID uuid) {
        return ResponseEntity.ok("user banned: " + uuid);
    }


    /* ============================
       POST MANAGEMENT
       ============================ */

    // View all posts
    @GetMapping("/posts")
    public ResponseEntity<String> getAllPosts() {
        return ResponseEntity.ok("list of posts");
    }

    // Delete a post (remove inappropriate content)
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable UUID postId) {
        return ResponseEntity.ok("post deleted: " + postId);
    }

    // Force-hide or moderate a post (optional)
    @PostMapping("/post/hide/{postId}")
    public ResponseEntity<String> hidePost(@PathVariable UUID postId) {
        return ResponseEntity.ok("post hidden: " + postId);
    }


    /* ============================
       REPORT HANDLING
       ============================ */

    // View all reports
    @GetMapping("/reports")
    public ResponseEntity<String> getAllReports() {
        return ResponseEntity.ok("list of reports");
    }

    // Handle/resolve a report
    @PostMapping("/report/resolve/{reportId}")
    public ResponseEntity<String> resolveReport(@PathVariable UUID reportId) {
        return ResponseEntity.ok("report resolved: " + reportId);
    }

    // Admin action: delete reported user
    @PostMapping("/report/{reportId}/delete-user/{userId}")
    public ResponseEntity<String> deleteReportedUser(
            @PathVariable UUID reportId,
            @PathVariable UUID userId) {

        return ResponseEntity.ok("reported user deleted: " + userId);
    }

    // Admin action: delete reported post
    @PostMapping("/report/{reportId}/delete-post/{postId}")
    public ResponseEntity<String> deleteReportedPost(
            @PathVariable UUID reportId,
            @PathVariable UUID postId) {

        return ResponseEntity.ok("reported post deleted: " + postId);
    }
}
