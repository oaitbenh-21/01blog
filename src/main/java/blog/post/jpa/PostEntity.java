package blog.post.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import blog.comment.jpa.CommentEntity;
import blog.like.jpa.PostLikeEntity;
import blog.user.jpa.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "_posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String content;
    private Long comment_count;
    @Column(nullable = false)
    private boolean hidden;
    @Column(nullable = false)
    private boolean banned;
    @ManyToOne
    private UserEntity author;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastUpdateTime;
    // likes
    @OneToMany(mappedBy = "post")
    private List<PostLikeEntity> likes;
    // comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    List<CommentEntity> comments;

    @PrePersist
    protected void onCreate() {
        this.creationTime = LocalDateTime.now();
    }
}
