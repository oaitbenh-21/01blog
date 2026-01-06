package blog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.Model.Media;
import blog.Model.Post;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {

    List<Media> findByPost(Post post);

    void deleteByPost(Post post);
}
