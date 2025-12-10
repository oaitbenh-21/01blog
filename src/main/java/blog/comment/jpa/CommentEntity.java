package blog.comment.jpa;

import java.util.UUID;

import org.hibernate.annotations.ManyToAny;

import blog.post.jpa.PostEntity;
import blog.user.jpa.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "_comments")
@Data
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToAny
    private PostEntity post;
    @ManyToAny
    private UserEntity author;

}
