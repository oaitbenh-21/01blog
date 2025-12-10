package blog.user.app.admin;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.post.dto.PostListResponse;
import blog.post.dto.PostResponse;
import blog.post.jpa.PostRepository;
import blog.user.dto.ProfileCard;
import blog.user.jpa.UserEntity;
import blog.user.jpa.UserRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class Admin {

    private final PostRepository postRepo;
    private final UserRepository userRepo;

    // View all users
    @GetMapping("/users")
    public ResponseEntity<List<ProfileCard>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll().stream().map(ProfileCard::fromEntity).toList());
    }

    // Ban a user
    @DeleteMapping("/user/ban/{uuid}")
    public ResponseEntity<String> banUser(@PathVariable UUID uuid) {
        userRepo.findById(uuid).get().setBanned(true);
        return ResponseEntity.ok("user banned: " + uuid);
    }

    // Delete a user
    @PostMapping("/user/{uuid}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuid) {
        UserEntity user = userRepo.findById(uuid).get();
        userRepo.delete(user);
        return ResponseEntity.ok("user deleted: " + uuid);
    }

    // View all posts
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = PostListResponse.mapToPostResponseList(postRepo.findAll());
        return ResponseEntity.ok(posts);
    }

    // Delete a post (remove inappropriate content)
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable UUID postId) {
        postRepo.delete(postRepo.findById(postId).get());
        return ResponseEntity.ok("post deleted succesfully : " + postId);
    }

    // Force-hide or moderate a post
    @PostMapping("/post/hide/{postId}")
    public ResponseEntity<String> hidePost(@PathVariable UUID postId) {
        postRepo.findById(postId).get().setHidden(true);
        return ResponseEntity.ok("post hidden: " + postId);
    }

    // Force-unhide a post
    @PostMapping("/post/unhide/{postId}")
    public ResponseEntity<String> unHidePost(@PathVariable UUID postId) {
        postRepo.findById(postId).get().setHidden(false);
        return ResponseEntity.ok("post hidden: " + postId);
    }

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

    // Handle/resolve a report
    @PostMapping("/report/delete/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable UUID reportId) {
        return ResponseEntity.ok("report deleted: " + reportId);
    }
}
