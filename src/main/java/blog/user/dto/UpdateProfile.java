package blog.user.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfile {
    private MultipartFile avatar;
    @NotEmpty(message = "fullname is mendatory")
    @NotBlank(message = "fullname is mendatory")
    @Size(min = 4, message = "fullname should be More Then 3 characters")
    private String fullname;
    @Email(message = "Invalid email format")
    private String email;
    @NotEmpty(message = "bio is mendatory")
    @NotBlank(message = "bio is mendatory")
    @Size(min = 4, message = "bio should be More Then 3 characters")
    private String bio;
}
