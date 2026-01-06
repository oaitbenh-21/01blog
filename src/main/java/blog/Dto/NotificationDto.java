package blog.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long id;
    private Long userId; // who receives it
    private String message;
    private boolean read = false;
    private LocalDateTime createdAt;
}
