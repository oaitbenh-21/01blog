package blog.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import blog.Dto.CommentDto;
import blog.Dto.PostDto;
import blog.Dto.PostResponseDto;
import blog.Model.Post;
import blog.Services.PostService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // ------------------------
    // Create a new post
    // ------------------------
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostDto postDto) {
        Post createdPost = postService.createPost(postDto);
        return ResponseEntity.ok(PostResponseDto.from(createdPost));
    }

    // ------------------------
    // Get all posts (feed)
    // ------------------------
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Post> posts = postService.getAllPosts(page, size);
        List<PostResponseDto> postDtos = posts.stream()
                .map(PostResponseDto::from) // call the static from() for each Post
                .collect(Collectors.toList());

        return ResponseEntity.ok(postDtos);
    }

    // ------------------------
    // Get post by ID
    // ------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    // ------------------------
    // Update post
    // ------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id,
            @RequestPart("post") PostDto postDto,
            @RequestPart(value = "media", required = false) List<MultipartFile> mediaFiles) {
        Post updatedPost = postService.updatePost(id, postDto, mediaFiles);
        return ResponseEntity.ok(updatedPost);
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
