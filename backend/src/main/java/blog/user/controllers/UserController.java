package blog.user.controllers;

import blog.user.dto.LoginRequest;
import blog.user.dto.RegisterRequest;
import blog.user.dto.UserDto;
import blog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserDto> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        // JWT implementation will be added later
        return ResponseEntity.ok("Login endpoint placeholder (JWT to be added)");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String avatarUrl) {
        return ResponseEntity.ok(userService.updateUser(id, bio, avatarUrl));
    }
}
