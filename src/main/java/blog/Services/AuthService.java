package blog.Services;

import org.springframework.stereotype.Service;

import blog.Dto.AuthResponse;
import blog.Dto.LoginRequest;
import blog.Dto.RegisterRequest;
import blog.Model.CustomUserDetails;
import blog.Model.User;
import blog.Model.enums.Role;
import blog.Repositories.UserRepository;
import blog.Security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService; // Assume you have JWT util for token generation

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already in use");
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already in use");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty())
            userOpt = userRepository.findByUsername(request.getEmail());
        if (userOpt.isEmpty())
            throw new RuntimeException("Invalid credentials");

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials");
        CustomUserDetails customUser = new CustomUserDetails(user);

        String token = jwtService.generateToken(customUser);

        AuthResponse response = new AuthResponse();
        response.setAccessToken(token);
        return response;
    }

}
