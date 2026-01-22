package blog.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReportRequest {

    private Long userid;
    private Long postid;

    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 500, message = "Reason cannot exceed 500 characters")
    private String reason;
}
