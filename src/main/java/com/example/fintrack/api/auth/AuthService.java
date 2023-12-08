package com.example.fintrack.api.auth;

import com.example.fintrack.api.common.exception.AuthenticationException;
import com.example.fintrack.api.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());
            return new LoginResponse(token);
        } catch (Exception e) {
            throw new AuthenticationException("Bad credentials");
        }
    }
}
