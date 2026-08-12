package com.biblio.app.user.dto;

import java.util.UUID;

import com.biblio.app.user.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class UserRoleUpdate {
    private UUID userId;
    private UserRole userRole;
}
