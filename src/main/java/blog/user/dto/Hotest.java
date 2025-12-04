package blog.user.dto;

import java.util.List;
import java.util.stream.Collectors;

import blog.user.jpa.UserEntity;
import lombok.Data;

@Data
public class Hotest {
    private List<ProfileCard> profiles;

    public static Hotest toHotest(List<UserEntity> users) {
        Hotest hotest = new Hotest();
        List<ProfileCard> profileList = users.stream()
                .map(ProfileCard::fromEntity)
                .collect(Collectors.toList());
        hotest.setProfiles(profileList);
        return hotest;
    }

}
