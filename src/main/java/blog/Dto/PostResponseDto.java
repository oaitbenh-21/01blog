package blog.Dto;

import blog.Model.Post;
import blog.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponseDto {
    private String content;
    private PostAuthor author;

    public static PostResponseDto from(Post post) {
        User user = post.getUser();
        return new PostResponseDto(post.getContent(), PostAuthor.from(user));
    }
}
