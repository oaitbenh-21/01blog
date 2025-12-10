package blog.post.app.comment.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {

    @NotBlank(message = "Content cannot be empty")
    @Size(max = 2000, message = "Comment cannot exceed 2000 characters")
    private String content;
    @NotNull(message = "Post ID is required")
    private UUID postId;

    public CommentDTO() {
    }

    public CommentDTO(String content, UUID postId) {
        this.content = content;
        this.postId = postId;
    }

}
