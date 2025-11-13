package blog.services.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.services.users.models.user;

public interface UserRepository extends JpaRepository<user, Long> {
    @Override
    user getById(Long id);
}
