package blog.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.Dto.NotificationDto;
import blog.Services.NotificationService;
import blog.Services.UserService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications() {
        return ResponseEntity.ok(notificationService.getNotificationDtos(userService.getCurrentUser()));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unread")
    public ResponseEntity<Void> markAsUnRead(@PathVariable Long id) {
        notificationService.markAsUnRead(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/markAllRead")
    public ResponseEntity<Void> markAllAsRead() {
        notificationService.markAllAsRead(userService.getCurrentUser());
        return ResponseEntity.ok().build();
    }
}
