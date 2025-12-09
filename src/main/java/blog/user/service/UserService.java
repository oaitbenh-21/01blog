package blog.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import blog.user.dto.UpdateProfile;
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

    public void updateUser(UpdateProfile updateProfile, UserEntity user) {
        // validate and update profile picture if provided
        // update avatar
        if (updateProfile.getAvatar() != null && !updateProfile.getAvatar().isEmpty()) {
            // logic to store the avatar file and get its path or URL
        }
        userRepo.updateUser(user.getId(), updateProfile.getBio(), updateProfile.getFullname(),
                updateProfile.getEmail());
    }
}
