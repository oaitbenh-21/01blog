package blog.Services;

import blog.Dto.CommentDto;
import blog.Dto.CommentResponseDto;
import blog.Dto.PostDto;
import blog.Model.*;
import blog.Model.enums.NotificationType;
import blog.Model.enums.Role;
import blog.Repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;
    private final LikeRepository likeRepository;
    private final MediaService mediaService;

    // --- Create a new post ---
    @Transactional
    public Post createPost(PostDto postDto) {
        User user = userService.getCurrentUser();
        if (user.isBanned()) {
            throw new RuntimeException("Your account is banned");
        }
        Post post = new Post();
        post.setUser(user);
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        post.setLikes(new ArrayList<>());
        post.setComments(new ArrayList<>());
        post.setMedia(new ArrayList<>());
        post = postRepository.save(post);

        // Save media
        if (postDto.getFiles() != null && !postDto.getFiles().isEmpty()) {
            for (String fileString : postDto.getFiles()) {
                if (fileString == null || fileString.isBlank()) {
                    continue;
                } else if (fileString.length() > 10_000_000) {
                    throw new RuntimeException("file size exceeds the maximum allowed limit of 10MB");
                }
                try {
                    String fileUrl = "media/" + System.currentTimeMillis() + UUID.randomUUID();
                    mediaService.saveBase64File(post, fileString, fileUrl);
                } catch (Exception e) {
                    postRepository.delete(post);
                    throw new RuntimeException("Failed to save media file. Invalid data.", e);
                }
            }
        }

        // notify followers
        List<User> followers = subscriptionRepository.findByFollowingId(user.getId())
                .stream().map(Subscription::getFollower).toList();
        for (User usr : followers) {
            notificationService.createNotification(usr, NotificationType.NEW_POST,
                    user.getUsername() + " made a new post");
        }

        return post;
    }

    // --- Get all posts ---
    public List<Post> getAllPosts() {
        return new ArrayList<>(postRepository.findByVisibleTrueOrderByCreatedAtDesc());
    }

    // --- Get posts from followed users ---
    public List<Post> getByFollowedUsers() {
        User currentUser = userService.getCurrentUser();
        return postRepository.findSubscriptionsPosts(currentUser);
    }

    // --- Get post by ID ---
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // --- Update post content and media ---
    @Transactional
    public Post updatePost(Long id, PostDto postDto) {
        Post post = getPostById(id);
        User user = userService.getCurrentUser();
        if (user.isBanned()) {
            throw new RuntimeException("Your account is banned");
        }
        if (!user.getId().equals(post.getUser().getId())) {
            throw new SecurityException("Unauthorized: Cannot edit this post");
        }

        // Update text
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());

        // Delete old media
        if (post.getMedia() != null && !post.getMedia().isEmpty()) {
            post.getMedia().forEach(media -> mediaService.deleteFile(media.getUrl()));
            post.getMedia().clear(); // orphanRemoval deletes from DB
        }

        // Save new media
        if (postDto.getFiles() != null && !postDto.getFiles().isEmpty()) {
            for (String fileString : postDto.getFiles()) {
                try {
                    String fileUrl = "media/" + System.currentTimeMillis() + UUID.randomUUID();
                    mediaService.saveBase64File(post, fileString, fileUrl);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to save media file. Post update aborted.", e);
                }
            }
        }

        return postRepository.save(post);
    }

    // --- Delete a post ---
    @Transactional
    public void deletePost(Long id) {
        Post post = getPostById(id);
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getId().equals(post.getUser().getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new RuntimeException("You are not allowed to delete this post");
        }

        // Delete media files
        if (post.getMedia() != null && !post.getMedia().isEmpty()) {
            post.getMedia().forEach(media -> mediaService.deleteFile(media.getUrl()));
        }

        postRepository.delete(post); // orphanRemoval ensures DB deletion
    }

    // --- Toggle visibility ---
    @Transactional
    public void toggleVisible(Long id) {
        Post post = getPostById(id);
        post.setVisible(!post.isVisible());
        postRepository.save(post);
    }

    // --- Toggle like ---
    @Transactional
    public void toggleLike(Long postId) {
        Post post = getPostById(postId);
        User user = userService.getCurrentUser();
        if (likeRepository.existsByPostIdAndUserId(postId, user.getId())) {
            likeRepository.findByPostAndUser(post, user)
                    .ifPresent(likeRepository::delete);
        } else {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
            if (!user.getId().equals(post.getUser().getId())) {
                notificationService.createNotification(post.getUser(), NotificationType.LIKE,
                        user.getUsername() + " liked your post");
            }
        }
    }

    // --- Add comment ---
    @Transactional
    public CommentResponseDto addComment(Long postId, CommentDto commentDto) {
        Post post = getPostById(postId);
        User user = userService.getCurrentUser();
        if (user.isBanned()) {
            throw new RuntimeException("Your account is banned");
        }
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(commentDto.getContent());
        comment = commentRepository.save(comment);

        CommentResponseDto res = CommentResponseDto.from(comment,
                user.getId().equals(comment.getUser().getId()));

        if (!user.getId().equals(post.getUser().getId())) {
            notificationService.createNotification(post.getUser(), NotificationType.COMMENT,
                    user.getUsername() + " commented on your post");
        }

        return res;
    }

    // --- Delete comment ---
    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        commentRepository.findById(commentId)
                .ifPresent(commentRepository::delete);
    }
}
