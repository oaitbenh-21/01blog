package blog.user;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.user.dto.Hotest;
import blog.user.dto.ProfileCard;

@RestController
@RequestMapping("/api/user/")
public class UserController {
    @GetMapping("/{uuid}")
    public ProfileCard profile(@PathVariable UUID uuid) {
        // call the user Service and get userProfile with data like
        // is the current user follow the specific user
        // check that uuid is valid uuid and check
        // is the user get himself's profile page
        return new ProfileCard();
    }

    

    @GetMapping("/hotest")
    public Hotest hostest() {
        // Get 20 hotest profile from user Service
        return new Hotest();
    }
    // add add user method
    // update user method
    // remove user method
}