package com.biblio.app.auth.dto;

import com.biblio.app.user.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private String tokenType;
    private String email;
    private UserRole role;
}
