package blog.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import blog.Model.Post;
import blog.Model.User;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserAndVisibleTrueOrderByCreatedAtDesc(User user);

    List<Post> findByVisibleTrueOrderByCreatedAtDesc();

    @Query("SELECT p FROM Post p WHERE p.visible = false AND p.user IN " +
            "(SELECT s.following FROM Subscription s WHERE s.follower = :user) " +
            "ORDER BY p.createdAt DESC")
    List<Post> findSubscriptionsPosts(@Param("user") User user);

}
