package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import blog.Dto.CommentDto;
import blog.Dto.PostDto;
import blog.Model.Comment;
import blog.Model.Like;
import blog.Model.Post;
import blog.Model.User;
import blog.Model.enums.Role;
import blog.Repositories.CommentRepository;
import blog.Repositories.LikeRepository;
import blog.Repositories.PostRepository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        post.setDescription(postDto.getDescription());
        List<Like> likes = List.of();
        post.setLikes(likes);
        List<Comment> comments = List.of();
        post.setComments(comments);
        post = postRepository.save(post);

        String outputDir = "../../../../../public"; // Directory to save files
        try {
            Files.createDirectories(Paths.get(outputDir));
            System.out.println("Directory created or already exists!");
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }

        String mediaRegex = "(?:!\\[[^\\]]*\\]|\\[[^\\]]*\\])\\(([^)\\s]+\\.(?:png|jpg|jpeg|gif|mp4|mov|webm|avi))(?:\\s+\"[^\"]*\")?\\)";
        Pattern pattern = Pattern.compile(mediaRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(postDto.getContent());

        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String url = matcher.group(1);
            String ext = getExtension(url);
            if (ext.equals("jpeg"))
                ext = "jpg";
            String uuidFilename = UUID.randomUUID().toString() + "." + ext;
            Path outputPath = Paths.get(outputDir, uuidFilename);
            try {
                saveUrlToFile(url, outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String replacement = matcher.group(0).replace(url, outputPath.toString().replace("\\", "/"));
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);
        return post;
    }

    private static String getExtension(String url) {
        int lastDot = url.lastIndexOf('.');
        if (lastDot != -1 && lastDot < url.length() - 1) {
            return url.substring(lastDot + 1).toLowerCase();
        }
        return "bin";
    }

    private static void saveUrlToFile(String urlStr, Path path) throws IOException {
        URL url = new URL(urlStr);
        try (InputStream in = url.openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public List<Post> getAllPosts(int page, int size) {
        return postRepository.findByIsDeletedFalse(PageRequest.of(page, size)).toList();
    }

    // public Page<Post> getByFollowedUsers(Pageable pageable) {
    // User currentUser = userService.getCurrentUser();

    // // Map subscriptions to the users being followed
    // List<User> followedUsers = currentUser.getFollowing().stream()
    // .map(sub -> sub.getFollowing())
    // .toList();

    // return postRepository.findByUserInAndIsDeletedFalse(followedUsers, pageable);
    // }

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
        post.setDescription(postDto.getDescription());
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
