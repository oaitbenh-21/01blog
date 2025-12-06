package blog.post.jpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import blog.like.jpa.PostLikeEntity;
import blog.user.jpa.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_posts")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String content;
    @OneToMany(mappedBy = "post")
    private List<PostLikeEntity> likes;
    private Long comments;
    @ManyToOne
    private UserEntity author;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastUpdateTime;
}
