package blog.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserDto {

    @Size(min = 4, max = 100, message = "Username must be between 4 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "Username can only contain letters, numbers, dots, and underscores")
    private String username;

    @Size(min = 7, max = 100, message = "Email must be between 7 and 100 characters")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 5_600_000, message = "Avatar image must be smaller than 4MB")
    private String avatar;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;
}
