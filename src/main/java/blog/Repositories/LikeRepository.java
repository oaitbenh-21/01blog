package blog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.Model.Like;
import blog.Model.Post;
import blog.Model.User;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);

    long countByPost(Post post);

    boolean existsByPostAndUser(Post post, User user);
}
