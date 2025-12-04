package blog.post.dto;

import java.util.UUID;

import blog.user.jpa.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
    private UUID id;
    private String username;
    private String fullname;
    private String avatar;

    public AuthorResponse(UserEntity user) {
        if (user != null) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
            this.avatar = user.getAvatar();
        }
    }

}