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
    private String content;
    private PostAuthor author;
    private int likes;
    private List<CommentResponseDto> comments;

    public static PostResponseDto from(Post post) {
        User user = post.getUser();
        List<Comment> postComments = post.getComments();
        if (postComments == null) {
            return new PostResponseDto(post.getContent(), PostAuthor.from(user), post.getLikes().size(), List.of());
        }
        List<CommentResponseDto> comments = postComments.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
        int likesCount = 0;
        if (post.getLikes() != null) {
            likesCount = post.getLikes().size();
        }
        return new PostResponseDto(post.getContent(), PostAuthor.from(user), likesCount, comments);
    }
}
