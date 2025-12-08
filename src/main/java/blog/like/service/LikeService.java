package blog.like.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import blog.like.jpa.PostLikeEntity;
import blog.like.jpa.PostLikeRepository;
import blog.post.jpa.PostEntity;
import blog.post.jpa.PostRepository;
import blog.user.jpa.UserEntity;
import blog.user.jpa.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LikeService {
    private final PostLikeRepository repository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void likePost(UUID post_id, UUID user_id) {
        UserEntity user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user_id));
        PostEntity post = postRepository.findById(post_id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + post_id));
        if (repository.existsByUser_IdAndPost_Id(user_id, post_id)) {
            PostLikeEntity like = repository.findByPost_IdAndUser_Id(post_id, user_id)
                    .orElseThrow(() -> new RuntimeException("The like found but actualy it's not found"));
            repository.delete(like);
        } else {
            repository.save(PostLikeEntity.builder().user(user).post(post).build());
        }
    }

}
