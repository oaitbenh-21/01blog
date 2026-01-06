package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Get current logged-in user
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    // Update current user profile
    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(@RequestBody User updatedUser) {
        return ResponseEntity.ok(userService.updateProfile(updatedUser));
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
    public ResponseEntity<List<User>> getSubscriptions() {
        return ResponseEntity.ok(userService.getSubscriptions());
    }

    // Get subscribers
    @GetMapping("/me/subscribers")
    public ResponseEntity<List<User>> getSubscribers() {
        return ResponseEntity.ok(userService.getSubscribers());
    }

    // Admin: delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
