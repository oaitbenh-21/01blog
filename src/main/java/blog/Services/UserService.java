package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import blog.Model.User;
import blog.Repositories.LikeRepository;
import blog.Repositories.SubscriptionRepository;
import blog.Repositories.UserRepository;
import blog.Dto.UserDto;
import blog.Model.Post;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public boolean postLikedByUser(Long postId) {
        return likeRepository.existsByPostIdAndUserId(postId, getCurrentUser().getId());
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Post> getPostsByUserId(Long userId) {
        User user = getUserById(userId);
        return user.getPosts();
    }

    public User UpdateProfile(UserDto updatedUser) {
        User user = getCurrentUser();
        if (!user.getId().equals(updatedUser.getId())) {
            throw new RuntimeException("Cannot update another user's profile");
        }
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username already in use");
            }
            user.setUsername(updatedUser.getUsername());
        }
        user.setBio(updatedUser.getBio());
        user.setAvatarUrl(updatedUser.getAvatar());
        return userRepository.save(user);
    }

    public boolean checkFollow(Long id) {
        User current = getCurrentUser();
        User toFollow = getUserById(id);
        if (subscriptionRepository.existsByFollowerAndFollowing(current, toFollow)) {
            return true;
        }
        return false;
    }

    // public void subscribe(Long id) {
    // User current = getCurrentUser();
    // User toFollow = getUserById(id);
    // if (subscriptionRepository.existsByFollowerAndFollowing(current, toFollow))
    // throw new RuntimeException("Already subscribed");
    // subscriptionRepository.save(new Subscription(null, current, toFollow, null));
    // }

    // public void unsubscribe(Long id) {
    // User current = getCurrentUser();
    // User toUnfollow = getUserById(id);
    // subscriptionRepository.findByFollowerAndFollowing(current, toUnfollow)
    // .ifPresent(subscriptionRepository::delete);
    // }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}