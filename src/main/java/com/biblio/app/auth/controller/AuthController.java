package com.biblio.app.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblio.app.auth.dto.LoginRequest;
import com.biblio.app.auth.dto.LoginResponse;
import com.biblio.app.auth.service.AuthService;
import com.biblio.app.common.constant.ApiMessages;
import com.biblio.app.security.JwtBlacklistService;
import com.biblio.app.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request
    ) {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            return ResponseEntity
                    .badRequest()
                    .body(ApiMessages.AUTHORIZATION_TOKEN_REQUIRED);
        }

        String token = authHeader.substring(7);

        jwtBlacklistService.blacklistToken(
                token,
                jwtService.extractExpiration(token)
        );

        return ResponseEntity.ok(
                ApiMessages.LOGOUT_SUCCESSFUL
        );
    }


}
