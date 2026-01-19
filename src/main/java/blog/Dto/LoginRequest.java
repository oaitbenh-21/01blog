package blog.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Username or email is required")
    @Size(min = 4, max = 100, message = "Username must be between 6 and 100 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 7, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;
}
