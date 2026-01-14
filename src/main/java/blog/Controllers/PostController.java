package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import blog.Dto.CommentDto;
import blog.Dto.PostDto;
import blog.Dto.PostResponseDto;
import blog.Model.Post;
import blog.Services.PostService;
import blog.Services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    // ------------------------
    // Create a new post
    // ------------------------
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostDto postDto) {
        Post createdPost = postService.createPost(postDto);
        return ResponseEntity.ok(PostResponseDto.from(createdPost, userService.postLikedByUser(createdPost.getId())));
    }

    // ------------------------
    // Get all posts (feed)
    // ------------------------
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Post> posts = postService.getAllPosts(page, size);
        List<PostResponseDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(PostResponseDto.from(post, userService.postLikedByUser(post.getId())));
        }

        return ResponseEntity.ok(postDtos);
    }

    // ------------------------
    // Get posts by followed users
    // ------------------------
    // @GetMapping("/followed")
    // public ResponseEntity<List<PostResponseDto>> getByFollowedUsers(
    //         @PageableDefault(size = 10) Pageable pageable) {
    //     List<Post> posts = postService.getByFollowedUsers(pageable).toList();
    //     List<PostResponseDto> postDtos = new ArrayList<>();
    //     for (Post post : posts) {
    //         postDtos.add(PostResponseDto.from(post, userService.postLikedByUser(post.getId())));
    //     }
    //     return ResponseEntity.ok(postDtos);
    // }

    // ------------------------
    // Get post by ID
    // ------------------------
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(PostResponseDto.from(post, userService.postLikedByUser(post.getId())));
    }

    // ------------------------
    // Update post
    // ------------------------
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id,
            @RequestPart("post") PostDto postDto) {
        Post updatedPost = postService.updatePost(id, postDto);
        return ResponseEntity.ok(PostResponseDto.from(updatedPost, userService.postLikedByUser(updatedPost.getId())));
    }

    // ------------------------
    // Soft delete post
    // ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully.");
    }

    // ------------------------
    // Like/unlike a post
    // ------------------------
    @PostMapping("/{id}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long id) {
        postService.toggleLike(id);
        return ResponseEntity.ok("Toggled like on post.");
    }

    // ------------------------
    // Add a comment
    // ------------------------
    @PostMapping("/{id}/comment")
    public ResponseEntity<String> addComment(@PathVariable Long id,
            @RequestBody CommentDto commentDto) {
        postService.addComment(id, commentDto);
        return ResponseEntity.ok("Comment added.");
    }

    // ------------------------
    // Delete a comment
    // ------------------------
    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId,
            @PathVariable Long commentId) {
        postService.deleteComment(postId, commentId);
        return ResponseEntity.ok("Comment deleted.");
    }
}
