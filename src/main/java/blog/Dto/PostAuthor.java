package blog.Dto;

import blog.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostAuthor {
    private Long id;
    private String username;
    private String avatar;
    private String role;

    public static PostAuthor from(User user) {
        return new PostAuthor(user.getId(), user.getUsername(), user.getAvatarUrl(), user.getRole().name());
    }
}
