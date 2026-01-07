package blog.Dto;

import java.util.List;

import lombok.Data;

@Data
public class UserProfile {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String role;
    private List<PostResponseDto> posts;
}
