package blog.services.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.services.users.models.user;
import blog.services.users.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class userController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping("/add-user")
    public String index() {
        user user = new user();
        userRepository.save(user);
        return user.toString();
    }

    @RequestMapping("/{id}")
    public String getUserData(@PathVariable Long id) {
        return userRepository.getById(id).toString();
    }
}
