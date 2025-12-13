package blog.user.app.follow;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.user.app.follow.dto.FollowResponse;
import blog.user.app.follow.service.FollowService;
import blog.user.jpa.UserEntity;
import lombok.Data;

@Data
@Controller
@RequestMapping("user/")
public class FollowController {
    private final FollowService followService;

    // FOLLOW A USER
    @PostMapping("{targetUserId}/follow")
    public ResponseEntity<FollowResponse> followUser(
            @PathVariable UUID targetUserId,
            @AuthenticationPrincipal UserEntity currentUser) {
        FollowResponse response = followService.follow(currentUser, targetUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // UNFOLLOW A USER
    @DeleteMapping("{targetUserId}/follow")
    public ResponseEntity<?> unfollowUser(@PathVariable UUID targetUserId,
            @AuthenticationPrincipal UserEntity currentUser) {
        FollowResponse response = followService.unfollow(currentUser, targetUserId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // CHECK IF CURRENT USER FOLLOWS TARGET
    @GetMapping("{targetUserId}/is-following")
    public ResponseEntity<Boolean> isFollowing(@PathVariable UUID targetUserId,
            @AuthenticationPrincipal UserEntity currentUser) {
        return ResponseEntity.ok(followService.isFollowing(currentUser.getId(), targetUserId));
    }

    // GET FOLLOWERS OF A USER
    @GetMapping("{userId}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable UUID userId) {
        return ResponseEntity.ok("");
    }

    // GET USERS A USER IS FOLLOWING
    @GetMapping("{userId}/following")
    public ResponseEntity<?> getFollowing(@PathVariable UUID userId) {
        return ResponseEntity.ok("");
    }

    // GET POSTS FROM FOLLOWED USERS (FEED)
    // @GetMapping("feed")
    // public ResponseEntity<?> getUserFeed(
    // @AuthenticationPrincipal UserEntity currentUser) {
    // return ResponseEntity.ok("");
    // }
}
