package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.Dto.CommentDto;
import blog.Dto.CommentResponseDto;
import blog.Dto.PostDto;
import blog.Dto.PostResponseDto;
import blog.Model.Post;
import blog.Services.PostService;
import blog.Services.UserService;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostDto postDto) {
        Post createdPost = postService.createPost(postDto);
        return ResponseEntity.ok(PostResponseDto.from(createdPost, userService.postLikedByUser(createdPost.getId()),
                userService.getCurrentUser()));
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostResponseDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(PostResponseDto.from(post,
                    userService.postLikedByUser(post.getId()),
                    userService.getCurrentUser()));
        }

        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<PostResponseDto>> getSubscriptionsPosts() {
        List<Post> posts = postService.getByFollowedUsers();
        List<PostResponseDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(PostResponseDto.from(post, userService.postLikedByUser(post.getId()),
                    userService.getCurrentUser()));
        }
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(
                PostResponseDto.from(post, userService.postLikedByUser(post.getId()), userService.getCurrentUser()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
            @RequestBody PostDto postDto) {
        Post updatedPost = postService.updatePost(id, postDto);
        return ResponseEntity.ok(PostResponseDto.from(updatedPost, userService.postLikedByUser(updatedPost.getId()),
                userService.getCurrentUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long id) {
        postService.toggleLike(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentResponseDto> addComment(@PathVariable Long id,
            @RequestBody @Valid CommentDto commentDto) {
        return ResponseEntity.ok(postService.addComment(id, commentDto));
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,
            @PathVariable Long commentId) {
        postService.deleteComment(postId, commentId);
        return ResponseEntity.ok().build();
    }
}
