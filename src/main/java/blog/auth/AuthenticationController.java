package blog.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import blog.auth.dto.AuthenticateRequest;
import blog.auth.dto.AuthenticateResponse;
import blog.auth.dto.RegistrationRequest;
import blog.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
// @Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticateResponse> register(@RequestBody @Valid RegistrationRequest request) {
        AuthenticateResponse auth = service.register(request);
        return ResponseEntity.status(auth.getStatus()).body(auth);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticateResponse> authenticate(@RequestBody @Valid AuthenticateRequest req) {
        return ResponseEntity.ok(service.authenticate(req));
    }

}
