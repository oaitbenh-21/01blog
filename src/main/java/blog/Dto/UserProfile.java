package blog.Dto;

import java.util.List;

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
    private boolean follow;
    private boolean isMine;
    private List<PostResponseDto> posts;
    private String avatar;
    private String CDate;
    private boolean isBanned;
    private int followersCount;
    private int followingCount;

    public static UserProfile from(User user, List<PostResponseDto> posts, boolean follow, boolean isMine) {
        return UserProfile.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .role(user.getRole().name())
                .posts(posts)
                .follow(follow)
                .isMine(isMine)
                .avatar(user.getAvatarUrl())
                .CDate(user.getCreatedAt().toString())
                .isBanned(user.isBanned())
                .followersCount(user.getFollowers().size())
                .followingCount(user.getFollowing().size())
                .build();
    }

}
