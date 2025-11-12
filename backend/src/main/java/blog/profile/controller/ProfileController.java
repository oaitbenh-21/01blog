package blog.profile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.profile.model.ProfileModel;

@RestController
@RequestMapping("/profile/")
public class ProfileController {
    @GetMapping("/{id}")
    public ProfileModel getById(@PathVariable long id) {
        ProfileModel profile = new ProfileModel();
        return profile;
    }
}
