package blog.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotEmpty(message = "Fullname is mendatory")
    @NotBlank(message = "Fullname is mendatory")
    @Size(min = 4, max = 40, message = "Fullname should be Between 4 and 40 characters")
    private String fullname;
    @NotEmpty(message = "Username is mendatory")
    @NotBlank(message = "Username is mendatory")
    @Size(min = 4, message = "Username should be More Then 3 characters")
    private String username;
    @NotEmpty(message = "Password is mendatory")
    @NotBlank(message = "Password is mendatory")
    @Size(min = 8, max = 16, message = "Password should be Between 8 and 16 characters")
    private String password;
    @NotEmpty(message = "Email is mendatory")
    @NotBlank(message = "Email is mendatory")
    @Email(message = "Email Not Valid")
    private String email;

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

}
