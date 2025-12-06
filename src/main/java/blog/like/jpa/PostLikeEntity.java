package blog.like.jpa;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import blog.post.jpa.PostEntity;
import blog.user.jpa.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "_post_likes", uniqueConstraints = @UniqueConstraint(columnNames = { "post_id", "user_id" }))
public class PostLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;
}