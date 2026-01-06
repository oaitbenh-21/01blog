package blog.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import blog.Model.enums.MediaType;

@Entity
@Table(name = "media")
@Data
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Owning post
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    /**
     * IMAGE or VIDEO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MediaType type;

    /**
     * Extracted from markdown (![](...))
     */
    @Column(nullable = false, length = 255)
    private String url;

    /**
     * Audit
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}