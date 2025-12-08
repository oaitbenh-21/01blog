package blog.user.jpa;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import blog.like.jpa.PostLikeEntity;
import blog.post.jpa.PostEntity;
import blog.user.jpa.utils.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "_users")
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails, Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String avatar;
    private String fullname;
    @Column(unique = true)
    private String email;
    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String bio;
    private Long followers;
    private Long following;
    private Role role;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PostLikeEntity> liked;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationTime;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastUpdateTime;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PostEntity> posts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getName() {
        return this.username;
    }

}
