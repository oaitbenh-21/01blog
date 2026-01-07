package blog.Dto;

import java.util.List;
import java.util.stream.Collectors;

import blog.Model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfile {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String role;
    private List<PostResponseDto> posts;

    public static UserProfile from(User user) {
        return UserProfile.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .role(user.getRole().name())
                .posts(
                        user.getPosts()
                                .stream()
                                .map(PostResponseDto::from)
                                .collect(Collectors.toList()))
                .build();
    }

}
