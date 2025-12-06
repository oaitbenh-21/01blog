package blog.post.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import blog.post.dto.PostListResponse;
import blog.post.dto.PostRequest;
import blog.post.dto.PostResponse;
import blog.post.jpa.PostEntity;
import blog.post.jpa.PostRepository;
import blog.user.jpa.UserEntity;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository repository;

    public PostListResponse getAllPosts() {
        System.err.println(repository.findAll());
        return PostListResponse.builder().posts(PostListResponse.mapToPostResponseList(repository.findAll())).build();
    }

    public void createPost(PostRequest post_request, UserEntity user) {
        PostEntity post = PostEntity.builder()
                .content(post_request.getContent())
                .comments(0L)
                .author(user)
                .build();
        repository.save(post);
    }

    public PostResponse getPost(UUID id) {
        PostEntity post = repository.findById(id).orElseThrow(() -> new RuntimeException("Post Not Found"));
        return new PostResponse(post);
    }
}
