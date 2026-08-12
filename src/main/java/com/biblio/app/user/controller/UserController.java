package com.biblio.app.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblio.app.common.response.ResponseDto;
import com.biblio.app.common.constant.ApiMessages;
import com.biblio.app.user.dto.UserPasswordUpdate;
import com.biblio.app.user.dto.UserRequest;
import com.biblio.app.user.dto.UserRoleUpdate;
import com.biblio.app.user.dto.UserUpdateRequest;
import com.biblio.app.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        return ResponseEntity.ok(
                authentication.getAuthorities()
        );
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseDto getAllUser() {
        return ResponseDto.builder()
            .success(true)
            .message(ApiMessages.USERS_RETRIEVED)
            .data(userService.getAllUsers())
            .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@userSecurityService.canAccessUser(#id, authentication)")
    public ResponseDto getUserById(@PathVariable UUID id) {
        return ResponseDto.builder()
            .success(true)
            .message(ApiMessages.USER_RETRIEVED)
            .data(userService.getUserById(id))
            .build();
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    @GetMapping("/{email}")
    public ResponseDto getUserByEmail(@PathVariable String email) {
        return ResponseDto.builder()
            .success(true)
            .message(ApiMessages.USER_RETRIEVED)
            .data(userService.getUserByEmail(email))
            .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @GetMapping("/reader")
    public ResponseDto getAllReader() {
        return ResponseDto.builder()
            .success(true)
            .message(ApiMessages.USER_RETRIEVED)
            .data(userService.getAllReaders())
            .build();
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    public ResponseEntity<ResponseDto> saveUser(@RequestBody UserRequest entity, Authentication authentication) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResponseDto.builder()
                .success(true)
                .message(ApiMessages.USER_CREATED)
                .data(userService.createUser(entity, authentication))
                .build()
            );
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@userSecurityService.canUpdateUser(#id, authentication)")
    public ResponseDto updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest userRequest){
        return ResponseDto.builder()
            .success(true)
            .message(ApiMessages.USER_UPDATED)
            .data(userService.updateUser(id, userRequest))
            .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseDto deleteUser(@PathVariable UUID id){

        userService.deleteUser(id);
        
        return ResponseDto.builder()
            .success(true)
            .message(ApiMessages.USER_DELETED)
            .data(null)
            .build();
    }
    
    @PostMapping("/update-role")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseDto updateRole(@RequestBody UserRoleUpdate userRoleUpdate, Authentication authentication) {
        userService.updateRole(authentication, userRoleUpdate);
        return ResponseDto.builder()
                .success(true)
                .message(ApiMessages.USER_ROLE_UPDATED)
                .data(null)
                .build();
    }

    @PostMapping("/new-password/{id}")
    public ResponseDto updatePassword(@RequestBody UserPasswordUpdate userPasswordUpdate, Authentication authentication, UUID id) {
        userService.updatePassword(authentication, userPasswordUpdate, id);
        return ResponseDto.builder()
                .success(true)
                .message(ApiMessages.USER_PWD_UPDATED)
                .data(null)
                .build();
    }

}
