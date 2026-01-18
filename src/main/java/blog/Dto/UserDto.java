package blog.Dto;

import blog.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String avatar;
    private String email;
    private String bio;
    private String role;
    private String CDate;
    private boolean isBanned;

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getAvatarUrl(), user.getEmail(), user.getBio(),
                user.getRole().name(), user.getCreatedAt().toString(), user.isBanned());
    }
}
