package blog.like;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.like.service.LikeService;
import blog.user.jpa.UserEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/action")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService like_service;

    @GetMapping("/post/{uuid}")
    public ResponseEntity<String> likePost(@PathVariable UUID uuid, Authentication auth) {
        like_service.likePost(uuid, ((UserEntity) auth.getPrincipal()).getId());
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/comment/{uuid}")
    public ResponseEntity<String> likeComment(@PathVariable UUID uuid, Authentication auth) {
        // like_service.likePost(uuid, (UserEntity) auth.getPrincipal());
        // login not handled yet
        return ResponseEntity.ok("ok");
    }
}
