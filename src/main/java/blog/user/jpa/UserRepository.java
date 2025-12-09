package blog.user.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Override
    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String usernaem);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByFollowersGreaterThan(long followers);

    @Modifying
    @Query(value = "UPDATE _users SET bio = :bio, fullname = :fullname, email = :email WHERE id = :uuid", nativeQuery = true)
    void updateUser(@Param("uuid") UUID uuid, @Param("bio") String bio, @Param("fullname") String fullname,
            @Param("email") String email);

    @Query(value = "UPDATE _users SET avatar = :profile WHERE id = :uuid", nativeQuery = true)
    void updateProfile(@Param("uuid") UUID uuid, @Param("profile") String profile);

}