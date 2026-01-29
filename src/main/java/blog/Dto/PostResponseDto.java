package blog.Dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import blog.Model.Comment;
import blog.Model.Post;
import blog.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String content;
    private PostAuthor author;
    private int likes;
    private boolean likedByCurrentUser;
    private List<CommentResponseDto> comments;
    private String CDate;
    private String description;
    private List<String> fileUrl;
    private Boolean visible;
    private Boolean mine;

    public static PostResponseDto from(Post post, boolean likedByCurrentUser, User currentUser) {
        User user = post.getUser();
        List<Comment> postComments = post.getComments();
        List<String> files = new ArrayList<>();
        if (post.getMedia() != null) {
            System.out.println();
            System.out.println(post.getMedia());
            System.out.println();
            for (var media : post.getMedia()) {
                files.add(media.getUrl());
            }
        }

        if (postComments == null) {
            return new PostResponseDto(post.getId(), post.getContent(), PostAuthor.from(user), post.getLikes().size(),
                    likedByCurrentUser, List.of(), post.getCreatedAt().toString(), post.getDescription(), files,
                    post.isVisible(), currentUser.getId().equals(user.getId()));
        }
        List<CommentResponseDto> comments = postComments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
        int likesCount = 0;
        if (post.getLikes() != null) {
            likesCount = post.getLikes().size();
        }
        return new PostResponseDto(post.getId(), post.getContent(), PostAuthor.from(user), likesCount,
                likedByCurrentUser, comments, post.getCreatedAt().toString(), post.getDescription(), files,
                post.isVisible(), currentUser.getId().equals(user.getId()));
    }
}
