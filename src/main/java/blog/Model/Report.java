package blog.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import blog.Model.enums.ReportStatus;

@Entity
@Table(name = "reports")
@Data
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne
    @JoinColumn(name = "reported_by_id")
    private User reportedBy;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
