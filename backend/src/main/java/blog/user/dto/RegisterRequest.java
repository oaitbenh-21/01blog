package blog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @Size(min = 3, max = 40)
    private String username;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
}
