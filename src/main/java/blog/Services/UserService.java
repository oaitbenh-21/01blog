package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import blog.Model.User;
import blog.Repositories.SubscriptionRepository;
import blog.Repositories.UserRepository;
import blog.Model.Subscription;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateProfile(User updatedUser) {
        User user = getCurrentUser();
        user.setUsername(updatedUser.getUsername());
        user.setBio(updatedUser.getBio());
        user.setAvatarUrl(updatedUser.getAvatarUrl());
        return userRepository.save(user);
    }

    public void subscribe(Long id) {
        User current = getCurrentUser();
        User toFollow = getUserById(id);
        if(subscriptionRepository.existsByFollowerAndFollowing(current, toFollow))
            throw new RuntimeException("Already subscribed");
        subscriptionRepository.save(new Subscription(null, current, toFollow, null));
    }

    public void unsubscribe(Long id) {
        User current = getCurrentUser();
        User toUnfollow = getUserById(id);
        subscriptionRepository.findByFollowerAndFollowing(current, toUnfollow)
            .ifPresent(subscriptionRepository::delete);
    }

    public List<User> getSubscriptions() {
        return subscriptionRepository.findByFollower(getCurrentUser())
            .stream().map(sub -> sub.getFollowing()).toList();
    }

    public List<User> getSubscribers() {
        return subscriptionRepository.findByFollowing(getCurrentUser())
            .stream().map(sub -> sub.getFollower()).toList();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}