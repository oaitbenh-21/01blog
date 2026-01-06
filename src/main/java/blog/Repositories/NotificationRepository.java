package blog.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.Model.Notification;
import blog.Model.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByCreatedAtDesc(User user);

    long countByUserAndIsReadFalse(User user);
}
