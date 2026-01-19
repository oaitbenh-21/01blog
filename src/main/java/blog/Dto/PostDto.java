package blog.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class PostDto {

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 300, message = "Description should be from 10 to 300 characters")
    private String description;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 5_000_000, message = "Content should be from 10 to 5000 characters")
    private String content;

    @Size(max = 10_000_000, message = "File is too large")
    @Pattern(regexp = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$", message = "File content must be valid Base64")
    private String file;

}
