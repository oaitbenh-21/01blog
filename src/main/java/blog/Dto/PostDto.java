package blog.Dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 400, message = "Description should be from 10 to 400 characters")
    @Pattern(regexp = "^[\\x20-\\x7E]*$", message = "Description can only contain printable ASCII characters")
    private String description;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 2_000_000, message = "Content should be from 10 to 2,000,000 characters")
    private String content;

    @Size(max = 5, message = "You can attach at most 2 files")
    private List<String> files;
}
