package blog.Dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 300, message = "Description should be from 10 to 300 characters")
    private String description;

    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 5_000_000, message = "Content should be from 10 to 5000 characters")
    private String content;

    @Size(max = 10_000_000, message = "File is too large")
    private List<String> file;
}
