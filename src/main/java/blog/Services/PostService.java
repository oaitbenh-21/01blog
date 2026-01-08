package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import blog.Dto.CommentDto;
import blog.Dto.PostDto;
import blog.Model.Comment;
import blog.Model.Like;
import blog.Model.Media;
import blog.Model.Post;
import blog.Model.User;
import blog.Repositories.CommentRepository;
import blog.Repositories.LikeRepository;
import blog.Repositories.MediaRepository;
import blog.Repositories.PostRepository;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MediaRepository mediaRepository;

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

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public Post updatePost(Long id, PostDto postDto, List<MultipartFile> mediaFiles) {
        Post post = getPostById(id);
        post.setContent(postDto.getContent());
        Post updated = postRepository.save(post);

        if (mediaFiles != null) {
            mediaRepository.deleteByPost(post);
            for (MultipartFile file : mediaFiles) {
                Media media = new Media();
                media.setPost(updated);
                media.setUrl("uploaded_path/" + file.getOriginalFilename());
                media.setType(blog.Model.enums.MediaType.IMAGE); // Simplified
                mediaRepository.save(media);
            }
        }
        return updated;
    }

    public void deletePost(Long id) {
        Post post = getPostById(id);
        post.setDeleted(true);
        postRepository.save(post);
    }

    public void toggleLike(Long postId) {
        Post post = getPostById(postId);
        User user = userService.getCurrentUser();
        likeRepository.findByPostAndUser(post, user)
                .ifPresentOrElse(
                        likeRepository::delete,
                        () -> likeRepository.save(new Like(null, post, user, null)));
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
