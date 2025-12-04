package blog.post.jpa;

import java.util.UUID;

import blog.user.jpa.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "_posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String content;
    private Long likes;
    private Long comments;
    // private boolean liked; | i'll implement
    @ManyToOne
    private UserEntity author;
}
