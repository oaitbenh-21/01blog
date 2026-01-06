package blog.Dto;

import lombok.Data;

import java.util.Map;

@Data
public class AnalyticsDto {
    private long totalUsers;
    private long totalPosts;
    private long totalReports;
    private Map<String, Object> extraStats; // e.g., most reported users
}
