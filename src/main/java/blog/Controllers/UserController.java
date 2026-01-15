package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.Dto.PostResponseDto;
import blog.Dto.UserDto;
import blog.Dto.UserProfile;
import blog.Model.Post;
import blog.Services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get public profile (block page)
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUser(@PathVariable Long id) {
        List<Post> posts = userService.getPostsByUserId(id);
        List<PostResponseDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(PostResponseDto.from(post, userService.postLikedByUser(post.getId())));
        }
        return ResponseEntity.ok(UserProfile.from(userService.getUserById(id), postDtos, false,
                userService.getCurrentUser().getId() == id));
    }

    // Update current user profile
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateProfile(@RequestBody UserDto updatedUser) { // i should to edit requestbody to
                                                                                     // UserDto
        if (updatedUser.getId() == null || (updatedUser.getId() != userService.getCurrentUser().getId())) {
            throw new RuntimeException("User ID is required for update");
        }
        List<Post> posts = userService.getPostsByUserId(userService.UpdateProfile(updatedUser).getId());
        List<PostResponseDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(PostResponseDto.from(post, userService.postLikedByUser(post.getId())));
        }
        return ResponseEntity.ok(updatedUser);
    }

    // Subscribe to a user
    // @PostMapping("/{id}/subscribe")
    // public ResponseEntity<String> subscribe(@PathVariable Long id) {
    // if (id.equals(userService.getCurrentUser().getId())) {
    // throw new RuntimeException("Cannot subscribe to yourself");
    // }
    // userService.subscribe(id);
    // return ResponseEntity.ok("Subscribed successfully.");
    // }

    // Unsubscribe
    // @DeleteMapping("/{id}/unsubscribe")
    // public ResponseEntity<String> unsubscribe(@PathVariable Long id) {
    // if (id.equals(userService.getCurrentUser().getId())) {
    // throw new RuntimeException("Cannot unsubscribe to yourself");
    // }
    // userService.unsubscribe(id);
    // return ResponseEntity.ok("Unsubscribed successfully.");
    // }
}
