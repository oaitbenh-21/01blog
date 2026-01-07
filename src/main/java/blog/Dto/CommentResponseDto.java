package blog.Dto;

import blog.Model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private String content;
    private PostAuthor author;

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(comment.getContent(), PostAuthor.from(comment.getUser()));
    }
}
