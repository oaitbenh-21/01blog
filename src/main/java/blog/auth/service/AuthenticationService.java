package blog.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import blog.auth.dto.AuthenticateRequest;
import blog.auth.dto.AuthenticateResponse;
import blog.auth.dto.RegistrationRequest;
import blog.security.jwt.JwtService;
import blog.user.jpa.UserEntity;
import blog.user.jpa.UserRepository;
import blog.user.jpa.utils.Role;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwdEncoder;
    private final UserRepository repository;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public void register(RegistrationRequest request) {
        var user = UserEntity.builder()
                .fullname(request.getFullname())
                .username(request.getUsername())
                .password(passwdEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .build();
        repository.save(user);
    }

    public AuthenticateResponse authenticate(AuthenticateRequest req) {
        var auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        var user = ((UserEntity) auth.getPrincipal());
        var jwtToken = jwtService.generateToken(user);

        return AuthenticateResponse.builder()
                .token(jwtToken)
                .build();
    }

}
