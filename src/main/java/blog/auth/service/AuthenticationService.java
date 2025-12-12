package blog.auth.service;

import org.springframework.http.HttpStatus;
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
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwdEncoder;
    private final UserRepository repository;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthenticateResponse register(RegistrationRequest request) {
        AuthenticateResponse response;
        try {
            var user = UserEntity.builder()
                    .fullname(request.getFullname())
                    .username(request.getUsername())
                    .password(passwdEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .role(Role.USER)
                    .build();
            repository.save(user);
            response = AuthenticateResponse.builder().status(HttpStatus.CREATED).token(jwtService.generateToken(user))
                    .build();
        } catch (Exception e) {
            response = AuthenticateResponse.builder().status(HttpStatus.BAD_REQUEST).error(e.getMessage()).build();
        }
        return response;
    }

    public AuthenticateResponse authenticate(AuthenticateRequest req) {
        AuthenticateResponse response;
        try {
            var auth = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            req.getUsername(),
                            req.getPassword()));
            var user = ((UserEntity) auth.getPrincipal());
            var jwtToken = jwtService.generateToken(user);
            response = AuthenticateResponse.builder().status(HttpStatus.OK).token(jwtToken).build();
        } catch (IOException e) {
            response = AuthenticateResponse.builder().status(HttpStatus.BAD_REQUEST).error(e.getMessage()).build();
        }
        return response;
    }

}
