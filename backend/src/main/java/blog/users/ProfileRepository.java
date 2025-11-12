package blog.users;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.users.model.UserModel;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<UserModel, Long> {

    // Custom finder methods if needed
    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);

}
