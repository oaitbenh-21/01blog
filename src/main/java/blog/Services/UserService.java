package blog.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import blog.Model.User;
import blog.Repositories.LikeRepository;
import blog.Repositories.SubscriptionRepository;
import blog.Repositories.UserRepository;
import blog.Dto.UpdateUserDto;
import blog.Model.Post;
import blog.Model.Subscription;

import java.io.ByteArrayInputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
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

    public boolean checkFollow(Long id) {
        User current = getCurrentUser();
        User toFollow = getUserById(id);
        if (subscriptionRepository.existsByFollowerAndFollowing(current, toFollow)) {
            return true;
        }
        return false;
    }

    public void subscribe(Long id) {
        User current = getCurrentUser();
        User toFollow = getUserById(id);
        if (subscriptionRepository.existsByFollowerAndFollowing(current, toFollow))
            throw new RuntimeException("Already subscribed");
        subscriptionRepository.save(new Subscription(null, current, toFollow, null));
    }

    public void unsubscribe(Long id) {
        User current = getCurrentUser();
        User toUnfollow = getUserById(id);
        subscriptionRepository.findByFollowerAndFollowing(current, toUnfollow)
                .ifPresent(subscriptionRepository::delete);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateProfile(UpdateUserDto updatedUser) {
        User user = getCurrentUser();
        if (!user.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(updatedUser.getEmail());
        }
        if (!user.getUsername().equals(updatedUser.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username already in use");
            }
            user.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getBio() != null) {
            user.setBio(updatedUser.getBio());
        }
        String avatarData = updatedUser.getAvatar();
        if (avatarData != null && !avatarData.isEmpty()) {

            String uploadDir = "src/main/resources/static/avatars/";
            try {
                if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                    Path oldFile = Path.of(uploadDir + user.getAvatarUrl());
                    Files.deleteIfExists(oldFile);
                }
                String[] parts = avatarData.split(",");
                if (parts.length != 2)
                    throw new IllegalArgumentException("Invalid avatar data");
                byte[] fileBytes = Base64.getDecoder().decode(parts[1]);
                String mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(fileBytes));
                if (mimeType == null || !mimeType.startsWith("image/")) {
                    throw new IllegalArgumentException("Avatar must be an image");
                }
                String ext = mimeType.split("/")[1];
                String fileName = "avatar_" + user.getId() + "_" + System.currentTimeMillis() + "." + ext;
                Path filePath = Path.of(uploadDir + fileName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, fileBytes);
                user.setAvatarUrl("avatars/" + fileName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to save avatar: " + e.getMessage(), e);
            }
        }

        userRepository.save(user);
    }

}