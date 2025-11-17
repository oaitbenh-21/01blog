package blog.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignResponse {
    private String JwtToken;
    private String Error;
    private int StatusCode;
}
