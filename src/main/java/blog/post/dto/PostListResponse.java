package blog.post.dto;

import java.util.List;
import java.util.stream.Collectors;

import blog.post.jpa.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {
    private List<PostResponse> posts;

    public static List<PostResponse> mapToPostResponseList(List<PostEntity> posts) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

}
