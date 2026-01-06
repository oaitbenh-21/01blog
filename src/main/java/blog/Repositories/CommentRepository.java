package blog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.Model.Comment;
import blog.Model.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
}
