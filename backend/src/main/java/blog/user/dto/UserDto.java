package blog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank
    private String username;
    @Email
    @NotBlank
    private String email;
    private String bio;
    private String avatarUrl;
}
