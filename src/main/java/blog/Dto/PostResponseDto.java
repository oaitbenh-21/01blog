package blog.Dto;

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

    public static PostResponseDto from(Post post, boolean likedByCurrentUser) {
        User user = post.getUser();
        List<Comment> postComments = post.getComments();
        if (postComments == null) {
            return new PostResponseDto(post.getId(), post.getContent(), PostAuthor.from(user), post.getLikes().size(),
                    likedByCurrentUser, List.of(), post.getCreatedAt().toString());
        }
        List<CommentResponseDto> comments = postComments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
        int likesCount = 0;
        if (post.getLikes() != null) {
            likesCount = post.getLikes().size();
        }
        return new PostResponseDto(post.getId(), post.getContent(), PostAuthor.from(user), likesCount,
                likedByCurrentUser, comments, post.getCreatedAt().toString());
    }
}
