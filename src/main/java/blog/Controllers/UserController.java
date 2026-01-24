package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.Dto.PostResponseDto;
import blog.Dto.UpdateUserDto;
import blog.Dto.UserDto;
import blog.Dto.UserProfile;
import blog.Model.Post;
import blog.Model.User;
import blog.Services.UserService;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUser(@PathVariable Long id) {
        List<Post> posts = userService.getPostsByUserId(id);
        List<PostResponseDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            if (post.isVisible()) {
                postDtos.add(PostResponseDto.from(post, userService.postLikedByUser(post.getId())));
            }
        }
        return ResponseEntity.ok(UserProfile.from(userService.getUserById(id), postDtos, userService.checkFollow(id),
                userService.getCurrentUser().getId() == id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMine() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(UserDto.from(user));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(@RequestBody @Valid UpdateUserDto updatedUser) {
        userService.updateProfile(updatedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Void> subscribe(@PathVariable Long id) {
        if (id.equals(userService.getCurrentUser().getId())) {
            throw new RuntimeException("Cannot subscribe to yourself");
        }
        userService.subscribe(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long id) {
        if (id.equals(userService.getCurrentUser().getId())) {
            throw new RuntimeException("Cannot unsubscribe to yourself");
        }
        userService.unsubscribe(id);
        return ResponseEntity.ok().build();
    }
}
