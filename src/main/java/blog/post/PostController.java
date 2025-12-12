package blog.post;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.post.dto.PostListResponse;
import blog.post.dto.PostRequest;
import blog.post.dto.PostResponse;
import blog.post.service.PostService;
import blog.user.jpa.UserEntity;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService service;

    @RequestMapping("/all")
    public ResponseEntity<PostListResponse> getAllPosts() {
        PostListResponse posts = service.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<Authentication> searchPosts(Authentication auth) {
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody PostRequest postrequest, Authentication auth) {
        service.createPost(postrequest, (UserEntity) auth.getPrincipal());
        return ResponseEntity.ok("post created succesfully");
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<PostResponse> getPost(@PathVariable UUID uuid, Authentication auth) {
        return ResponseEntity.ok(service.getPost(uuid, (UserEntity) auth.getPrincipal()));
    }

}
