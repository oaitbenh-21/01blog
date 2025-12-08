package blog.post.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    @Override
    List<PostEntity> findAll();

    @Override
    Optional<PostEntity> findById(UUID id);
}
