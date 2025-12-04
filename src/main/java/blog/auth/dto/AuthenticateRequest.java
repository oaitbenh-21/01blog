package blog.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AuthenticateRequest {
    @NotEmpty(message = "Username is mendatory")
    @NotBlank(message = "Username is mendatory")
    @Size(min = 4, message = "Username should be More Then 3 characters")
    private String username;
    @NotEmpty(message = "Password is mendatory")
    @NotBlank(message = "Password is mendatory")
    @Size(min = 8, max = 16, message = "Password should be Between 8 and 16 characters")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
