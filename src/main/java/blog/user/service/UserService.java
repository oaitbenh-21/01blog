package blog.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import blog.user.jpa.UserEntity;
import blog.user.jpa.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    @Transactional
    public UserEntity loadUserByUsername(String Username) throws UsernameNotFoundException {
        return userRepo.findByUsername(Username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

}
