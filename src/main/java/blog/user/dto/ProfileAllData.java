package blog.user.dto;


import java.util.List;

import blog.post.dto.PostResponse;
import blog.user.jpa.UserEntity;
import lombok.Data;

@Data
public class ProfileAllData {
    private String bio;
    private String avatar;
    private String username;
    private String fullname;
    private Long followers;
    private Long following;
    private List<PostResponse> posts;

    public static ProfileAllData fromEntity(UserEntity entity) {
        ProfileAllData profile = new ProfileAllData();
        profile.setBio(entity.getBio());
        profile.setAvatar(entity.getAvatar());
        profile.setUsername(entity.getUsername());
        profile.setFullname(entity.getFullname());
        profile.setFollowers(entity.getFollowers());
        profile.setFollowing(entity.getFollowing());
        return profile;
    }

}