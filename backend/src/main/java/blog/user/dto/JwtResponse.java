package blog.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    @NotBlank
    private String JwtToken;
}
