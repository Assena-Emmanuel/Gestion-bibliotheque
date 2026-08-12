package com.biblio.app.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.biblio.app.auth.dto.LoginRequest;
import com.biblio.app.auth.dto.LoginResponse;
import com.biblio.app.security.JwtService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );

    String token = jwtService.generateToken(request.getEmail());

    return LoginResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .email(request.getEmail())
            .role(null)
            .build();
    }
}
