package blog.user.app.follow.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity, UUID> {
    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    Optional<FollowEntity> findByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    List<FollowEntity> findAllByFollowingId(UUID userId); // followers

    List<FollowEntity> findAllByFollowerId(UUID userId); // following

}
