package blog.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDto {
    private Long id;
    private Long reporterId;
    private Long reportedUserId;
    private String reason;
    private LocalDateTime createdAt;
    private String status;
}
