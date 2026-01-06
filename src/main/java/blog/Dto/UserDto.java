package blog.Dto;


import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String role; // USER or ADMIN
}
