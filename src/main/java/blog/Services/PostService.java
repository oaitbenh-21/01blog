package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import blog.Dto.CommentDto;
import blog.Dto.PostDto;
import blog.Model.Comment;
import blog.Model.Like;
import blog.Model.Post;
import blog.Model.Subscription;
import blog.Model.User;
import blog.Model.enums.Role;
import blog.Repositories.CommentRepository;
import blog.Repositories.LikeRepository;
import blog.Repositories.PostRepository;

import java.util.List;

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

    public Post createPost(PostDto postDto) {
        User user = userService.getCurrentUser();
        Post post = new Post();
        post.setUser(user);
        post.setContent(postDto.getContent());
        List<Like> likes = List.of();
        post.setLikes(likes);
        List<Comment> comments = List.of();
        post.setComments(comments);
        post = postRepository.save(post);

        // Handle media if provided
        return post;
    }

    public List<Post> getAllPosts(int page, int size) {
        return postRepository.findByIsDeletedFalse(PageRequest.of(page, size)).toList();
    }

    public Page<Post> getByFollowedUsers(Pageable pageable) {
        User currentUser = userService.getCurrentUser();

        List<User> followedUsers = currentUser.getFollowing().stream()
                .map(Subscription::getFollowing)
                .toList();

        return postRepository.findByUserInAndIsDeletedFalse(followedUsers, pageable);
    }

    public Post getPostById(Long id) {
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
        Post updated = postRepository.save(post);
        return updated;
    }

    public void deletePost(Long id) {
        Post post = getPostById(id);
        post.setDeleted(true);
        if (userService.getCurrentUser().getId() != post.getUser().getId()
                && userService.getCurrentUser().getRole() != Role.ADMIN) {
            throw new RuntimeException("Unauthorized");
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

    public void addComment(Long postId, CommentDto commentDto) {
        Post post = getPostById(postId);
        User user = userService.getCurrentUser();
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        commentRepository.delete(comment);
    }
}
