package com.biblio.app.user.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.biblio.app.user.entity.User;
import com.biblio.app.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserRepository userRepository;

    public boolean canAccessUser(
            UUID userId,
            Authentication authentication
    ) {

        // ADMIN ou MANAGER
        boolean isAdminOrManager =
                authentication.getAuthorities().stream()
                        .anyMatch(authority ->
                                authority.getAuthority().equals("ROLE_ADMIN")
                                || authority.getAuthority().equals("ROLE_MANAGER")
                        );

        if (isAdminOrManager) {
            return true;
        }

        // Utilisateur connecté
        User currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        return currentUser.getId().equals(userId);
    }
}
