package blog.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDto {

    @Size(min = 4, max = 100, message = "Username must be between 3 and 100 characters")
    private String username;

    @Size(min = 7, max = 100, message = "Email must be between 3 and 100 characters")
    @Email
    private String email;

    @Size(max = 2_000_000, message = "Avatar image is too large")
    private String avatar;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
}
