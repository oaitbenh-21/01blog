package blog.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

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
}
