package blog.post.app.comment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import blog.post.app.comment.dto.CommentDTO;
import blog.post.app.comment.jpa.CommentEntity;
import blog.post.app.comment.jpa.CommentRepository;
import blog.post.jpa.PostEntity;
import blog.post.jpa.PostRepository;
import blog.user.jpa.UserEntity;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final PostRepository postRepo;

    public CommentEntity createComment(
            UserEntity user,
            CommentDTO dto) {
        PostEntity post = postRepo.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if (!postRepo.isVisibleTo(post.getId(), user.getId())) {
            throw new RuntimeException("You are not allowed to comment on this post");
        }
        CommentEntity comment = new CommentEntity();
        comment.setAuthor(user);
        comment.setContent(dto.getContent());
        comment.setPost(post);
        return commentRepo.save(comment);
    }

    public CommentEntity updateComment(
            UserEntity user,
            UUID commentId,
            CommentDTO comment) {
        CommentEntity existing = commentRepo.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!existing.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to update this comment");
        }

        PostEntity post = postRepo.findById(comment.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        existing.setAuthor(user);
        existing.setContent(comment.getContent());
        existing.setPost(post);

        return commentRepo.save(existing);
    }

    public void deleteComment(UUID id) {
        commentRepo.deleteById(id);
    }

    public List<CommentEntity> getCommentsByPost(UUID postId) {
        return commentRepo.findByPostId(postId);
    }
}