package blog.user.service;

import blog.security.JwtService;
import blog.user.dto.LoginRequest;
import blog.user.dto.RegisterRequest;
import blog.user.dto.SignResponse;
import blog.user.dto.UserDto;
import blog.user.entity.User;
import blog.user.enums.Role;
import blog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public SignResponse login(LoginRequest request) {
        SignResponse response = userRepository.findByUsername(request.getUsername())
                .map(user -> {
                    String jwt = jwtService.generateToken(user.getUsername());
                    return SignResponse.builder()
                            .JwtToken(jwt)
                            .StatusCode(200)
                            .build();
                })
                .orElseGet(() -> SignResponse.builder()
                        .Error("invalid credential")
                        .StatusCode(401)
                        .build());
        return response;
    }

    public SignResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return SignResponse.builder().Error("Email is already taken").StatusCode(422).build();
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            return SignResponse.builder().Error("Username is already taken").StatusCode(422).build();
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);
        String jwt = jwtService.generateToken(saved.getUsername());
        return SignResponse.builder()
                .JwtToken(jwt)
                .StatusCode(200)
                .build();
    }

    public UserDto getUser(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDto updateUser(Long id, String bio, String avatarUrl) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);

        User updated = userRepository.save(user);

        return toDto(updated);
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
