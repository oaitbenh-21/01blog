package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.Dto.CommentDto;
import blog.Dto.CommentResponseDto;
import blog.Dto.PostDto;
import blog.Model.Comment;
import blog.Model.Like;
import blog.Model.Post;
import blog.Model.User;
import blog.Model.enums.Role;
import blog.Repositories.CommentRepository;
import blog.Repositories.LikeRepository;
import blog.Repositories.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private MediaService mediaService;

    public Post createPost(PostDto postDto) {
        User user = userService.getCurrentUser();
        Post post = new Post();
        post.setUser(user);
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        List<Like> likes = List.of();
        post.setLikes(likes);
        List<Comment> comments = List.of();
        post.setComments(comments);
        post = postRepository.save(post);
        if (postDto.getFile() == null || postDto.getFile().isEmpty()) {
            return post;
        }
        for (String fileString : postDto.getFile()) {
            try {
                String fileUrl = "media/" + System.currentTimeMillis() + UUID.randomUUID().toString();
                System.out.println();
                System.out.println(fileString.substring(0, 30));
                System.out.println();
                mediaService.saveBase64File(post, fileString, fileUrl);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                postRepository.delete(post);
                throw new RuntimeException("Failed to save media file, there is an uncorrect data.");
            }
        }
        return post;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>(postRepository.findByVisibleTrueOrderByCreatedAtDesc());
        return posts;
    }

    public List<Post> getByFollowedUsers() {
        User currentUser = userService.getCurrentUser();
        List<Post> posts = postRepository.findSubscriptionsPosts(currentUser);
        return posts;
    }

    public Post getPostById(Long id) {
        System.out.println(id);
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post updatePost(Long id, PostDto postDto) {
        if (postDto.getContent() == null || postDto.getContent().isBlank()) {
            throw new RuntimeException("Post content cannot be empty");
        }
        Post post = getPostById(id);
        if (userService.getCurrentUser().getId() != post.getUser().getId()) {
            throw new RuntimeException("Unauthorized");
        }
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        Post updated = postRepository.save(post);
        return updated;
    }

    public void deletePost(Long id) {
        Post post = getPostById(id);
        if (userService.getCurrentUser().getId() != post.getUser().getId()
                && userService.getCurrentUser().getRole() != Role.ADMIN) {
            throw new RuntimeException("You are not allowed to delete this post");
        }
        postRepository.delete(post);
    }

    public void toggleVisible(Long id) {
        Post post = getPostById(id);
        if (post.isVisible()) {
            post.setVisible(false);
        } else {
            post.setVisible(true);
        }
        postRepository.save(post);
    }

    public void toggleLike(Long postId) {
        Post post = getPostById(postId);
        User user = userService.getCurrentUser();
        if (likeRepository.existsByPostIdAndUserId(postId, user.getId())) {
            Like like = likeRepository.findByPostAndUser(post, user)
                    .orElseThrow(() -> new RuntimeException("Like not found"));
            likeRepository.delete(like);
        } else {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }
    }

    public CommentResponseDto addComment(Long postId, CommentDto commentDto) {
        Post post = getPostById(postId);
        User user = userService.getCurrentUser();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDto.getContent());
        comment = commentRepository.save(comment);
        CommentResponseDto res = CommentResponseDto.from(comment);
        return res;
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }
}
