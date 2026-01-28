package blog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.Model.Subscription;
import blog.Model.User;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByFollowerAndFollowing(User follower, User following);

    List<Subscription> findByFollowingId(Long userId);

    List<Subscription> findByFollowing(User following);

    boolean existsByFollowerAndFollowing(User follower, User following);

    long countByFollowing(User user);

    long countByFollower(User user);
}
