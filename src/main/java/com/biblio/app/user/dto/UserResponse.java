package com.biblio.app.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.biblio.app.user.enums.UserRole;
import com.biblio.app.user.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class UserResponse {
    private UUID id;
    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
    private String password;


    private String phone;

    private String address;

    private UserRole role;

    private UserStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
