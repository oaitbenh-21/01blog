package blog.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Size(min = 3, max = 40)
    private String username;
    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
}
