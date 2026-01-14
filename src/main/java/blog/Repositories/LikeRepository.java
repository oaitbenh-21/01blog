package blog.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.Model.Like;
import blog.Model.Post;
import blog.Model.User;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);

    boolean existsByPostIdAndUserId(Long postId, Long userId);

    long countByPost(Post post);

    boolean existsByPostAndUser(Post post, User user);
}
