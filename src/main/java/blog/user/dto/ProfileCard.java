package blog.user.dto;

import java.util.UUID;

import blog.user.jpa.UserEntity;
import lombok.Data;

@Data
public class ProfileCard {
    private UUID id;
    private String avatar;
    private String username;
    private Long followers;
    private Long following;

    public static ProfileCard fromEntity(UserEntity entity) {
        ProfileCard profile = new ProfileCard();
        profile.setId(entity.getId());
        profile.setAvatar(entity.getAvatar());
        profile.setFollowers(entity.getFollowers());
        profile.setFollowing(entity.getFollowing());
        return profile;
    }

}
