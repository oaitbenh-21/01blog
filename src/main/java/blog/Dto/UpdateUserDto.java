package blog.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDto {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 100, message = "Username must be between 3 and 100 characters")
    private String username;

    @NotBlank(message = "email is required")
    @Size(min = 7, max = 100, message = "Email must be between 3 and 100 characters")
    @Email
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    @Size(max = 2_000_000, message = "Avatar image is too large") // ~2MB Base64 string
    @Pattern(regexp = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$", message = "Avatar must be valid Base64")
    private String avatar;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
}
