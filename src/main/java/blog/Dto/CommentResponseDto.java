package blog.Dto;

import blog.Model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private PostAuthor author;
    private boolean mine;

    public static CommentResponseDto from(Comment comment, boolean mine) {
        return new CommentResponseDto(comment.getId(), comment.getContent(), PostAuthor.from(comment.getUser()), mine);
    }
}
