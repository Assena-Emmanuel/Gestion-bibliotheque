package com.biblio.app.user.dto;

import com.biblio.app.user.enums.UserRole;
import com.biblio.app.user.enums.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class UserRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private String address;

    private UserRole role;

    private UserStatus status;
}
