package blog.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import blog.Model.Post;
import blog.Model.User;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserAndIsDeletedFalse(User user, Pageable pageable);

    Page<Post> findByIsDeletedFalse(Pageable pageable);
}
