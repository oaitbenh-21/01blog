package blog.Dto;

import lombok.Data;
import java.time.LocalDateTime;

import blog.Model.Notification;

@Data
public class NotificationDto {
    private Long id;
    private String message;
    private boolean read = false;
    private LocalDateTime createdAt;

    public static NotificationDto from(Notification notifModel) {
        NotificationDto notification = new NotificationDto();
        notification.setId(notifModel.getId());
        notification.setRead(notifModel.isRead());
        notification.setMessage(notifModel.getMessage());
        notification.setCreatedAt(notifModel.getCreatedAt());
        return notification;
    }
}
