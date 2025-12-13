package blog.user.app.follow.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.user.app.follow.dto.FollowResponse;
import blog.user.app.follow.jpa.FollowEntity;
import blog.user.app.follow.jpa.FollowRepository;
import blog.user.jpa.UserEntity;
import blog.user.jpa.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class FollowService {

    private final FollowRepository followRepo;
    private final UserRepository userRepo;

    // * Follow a user
    public FollowResponse follow(UserEntity follower, UUID followingId) {

        if (follower.getId() == followingId) {
            return FollowResponse.builder().error("Users cannot followthemselves").status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        if (followRepo.existsByFollowerIdAndFollowingId(follower.getId(), followingId)) {
            return FollowResponse.builder().status(HttpStatus.ACCEPTED).error("User already followed").build();
        }

        Optional<UserEntity> followingOpt = userRepo.findById(followingId);

        if (followingOpt.isEmpty()) {
            return FollowResponse.builder().status(HttpStatus.BAD_REQUEST).error("User not found").build();
        }

        FollowEntity follow = FollowEntity.builder()
                .follower(follower)
                .following(followingOpt.get())
                .build();

        followRepo.save(follow);

        return FollowResponse.builder().status(HttpStatus.OK).error("Followed successfully").build();

    }

    // * Unfollow a user
    public FollowResponse unfollow(UserEntity follower, UUID followingId) {
        FollowResponse response;
        try {
            followRepo.findByFollowerIdAndFollowingId(follower.getId(), followingId)
                    .map(follow -> {
                        followRepo.delete(follow);
                        return true;
                    })
                    .orElseThrow(() -> new RuntimeException("you didnt Follow this user?"));
            response = FollowResponse.builder().status(HttpStatus.OK).build();
        } catch (Exception e) {
            response = FollowResponse.builder().status(HttpStatus.BAD_REQUEST).error(e.getMessage()).build();
        }
        return response;
    }

    // * Check if follower is following a user
    public boolean isFollowing(UUID followerId, UUID followingId) {
        return followRepo.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    // * Get all followers of a user (for profile block)
    public List<UserEntity> getFollowers(UUID userId) {
        return followRepo.findAllByFollowingId(userId)
                .stream()
                .map(FollowEntity::getFollower)
                .toList();
    }

    // * Get all users that a user follows
    public List<UserEntity> getFollowing(UUID userId) {
        return followRepo.findAllByFollowerId(userId)
                .stream()
                .map(FollowEntity::getFollowing)
                .toList();
    }

    // * Used when a post is published
    // * (notification trigger point)
    public List<UserEntity> getSubscribersForNotifications(UUID authorId) {
        return getFollowers(authorId);
    }
}
