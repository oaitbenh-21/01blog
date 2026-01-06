package blog.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDto {
    private Long id;
    private Long reporterId;
    private Long reportedUserId; // or postId
    private String reason;
    private LocalDateTime createdAt;
    private String status; // PENDING, RESOLVED, etc.
}
