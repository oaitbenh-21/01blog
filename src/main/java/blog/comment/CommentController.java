package blog.comment;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.comment.dto.CommentDTO;
import blog.comment.jpa.CommentEntity;
import blog.comment.service.CommentService;
import blog.user.jpa.UserEntity;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // CREATE
    @PostMapping
    public CommentEntity createComment(@RequestBody CommentDTO comment, Authentication auth) {
        return commentService.createComment((UserEntity) auth.getPrincipal(), comment);
    }

    // UPDATE
    @PutMapping("/{id}")
    public CommentEntity updateComment(
            @PathVariable UUID id,
            @RequestBody CommentDTO comment,
            Authentication auth) {
        return commentService.updateComment((UserEntity) auth.getPrincipal(), id, comment);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable UUID id) {
        commentService.deleteComment(id);
    }
}
