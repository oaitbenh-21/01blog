package blog.post.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostRequest {
    private String content;
    private List<MultipartFile> media;
}
