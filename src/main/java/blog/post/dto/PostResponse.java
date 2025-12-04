package blog.post.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import blog.post.jpa.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private UUID id;
    private String content;
    private Long likes;
    private Long comments;
    private AuthorResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean liked;

    public PostResponse(PostEntity post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.likes = post.getLikes();
        this.comments = post.getComments();
        if (post.getAuthor() != null) {
            this.author = new AuthorResponse(post.getAuthor());
        }

    }

}