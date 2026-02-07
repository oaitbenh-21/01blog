package blog.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 100, message = "Username must be between 4 and 100 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._]+$",
        message = "Username can only contain letters, numbers, dots, and underscores"
    )
    private String username;

    @NotBlank(message = "Email is required")
    @Size(min = 7, max = 100, message = "Email must be between 7 and 100 characters")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#]).+$",
        message = "Password must contain uppercase, lowercase, number, and special character"
    )
    private String password;
}
