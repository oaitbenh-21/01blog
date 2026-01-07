package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.Dto.UserProfile;
import blog.Model.User;
import blog.Services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get public profile (block page)
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserProfile.from(userService.getUserById(id)));
    }

    // Get current logged-in user
    @GetMapping("/me")
    public ResponseEntity<UserProfile> getCurrentUser() {
        return ResponseEntity.ok(UserProfile.from(userService.getCurrentUser()));
    }

    // Update current user profile
    @PutMapping("/me")
    public ResponseEntity<UserProfile> updateProfile(@RequestBody User updatedUser) {
        return ResponseEntity.ok(UserProfile.from(userService.updateProfile(updatedUser)));
    }

    // Subscribe to a user
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<String> subscribe(@PathVariable Long id) {
        userService.subscribe(id);
        return ResponseEntity.ok("Subscribed successfully.");
    }

    // Unsubscribe
    @DeleteMapping("/{id}/unsubscribe")
    public ResponseEntity<String> unsubscribe(@PathVariable Long id) {
        userService.unsubscribe(id);
        return ResponseEntity.ok("Unsubscribed successfully.");
    }

    // Get subscriptions
    @GetMapping("/me/subscriptions")
    public ResponseEntity<List<UserProfile>> getSubscriptions() {
        List<User> users = userService.getSubscriptions();
        if (users == null) {
            return ResponseEntity.ok(List.of());
        }
        List<UserProfile> userProfiles = users.stream().map(UserProfile::from).toList();
        return ResponseEntity.ok(userProfiles);
    }

    // Get subscribers
    @GetMapping("/me/subscribers")
    public ResponseEntity<List<UserProfile>> getSubscribers() {
        List<User> users = userService.getSubscribers();
        if (users == null) {
            return ResponseEntity.ok(List.of());
        }
        List<UserProfile> userProfiles = users.stream().map(UserProfile::from).toList();

        return ResponseEntity.ok(userProfiles);
    }

    // Admin: delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
