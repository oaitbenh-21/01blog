package blog.post.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    @Override
    List<PostEntity> findAll();

    @Override
    Optional<PostEntity> findById(UUID id);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM _posts post WHERE post.id = :post_id AND post.author = :user_id", nativeQuery = true)
    boolean isVisibleTo(UUID post_id, UUID user_id);

}
