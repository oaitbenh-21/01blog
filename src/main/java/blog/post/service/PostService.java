package blog.post.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import blog.post.dto.PostListResponse;
import blog.post.jpa.PostRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository repository;

    public PostListResponse getAllPosts(Pageable pageable) {
        return PostListResponse.builder().posts(PostListResponse.mapToPostResponseList(repository.findAll())).build();
    }
}
