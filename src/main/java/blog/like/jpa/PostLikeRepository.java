package blog.like.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

    List<PostLikeEntity> findByUser_Id(UUID userId);

    Optional<PostLikeEntity> findByPost_IdAndUser_Id(UUID postId, UUID userId);

    long countByPost_Id(UUID postId);

    boolean existsByUser_IdAndPost_Id(UUID userId, UUID postId);
}
