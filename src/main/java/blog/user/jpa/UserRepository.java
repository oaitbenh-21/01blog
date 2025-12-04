package blog.user.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Override
    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String usernaem);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByFollowersGreaterThan(long followers);

    // @Query("SELECT e FROM users e WHERE 1 > :var")
    // List<EntityClass> CustomQuetyMethod(@Param("var") String value);

}