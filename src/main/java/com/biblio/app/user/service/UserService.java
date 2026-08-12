package com.biblio.app.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.biblio.app.user.dto.UserPasswordUpdate;
import com.biblio.app.user.dto.UserRequest;
import com.biblio.app.user.dto.UserResponse;
import com.biblio.app.user.dto.UserRoleUpdate;
import com.biblio.app.user.dto.UserUpdateRequest;
import com.biblio.app.user.entity.User;
import com.biblio.app.user.enums.UserRole;
import com.biblio.app.user.repository.UserRepository;
import com.biblio.app.common.constant.ApiMessages;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private UserResponse toUserRespose(User user){
        return UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .address(user.getAddress())
            .password(user.getPassword())
            .status(user.getStatus())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
        .build();
    }

    public UserResponse createUser(UserRequest userRequest, Authentication authentication){

        String currentRole = authentication.getAuthorities()
            .iterator()
            .next()
            .getAuthority();

        UserRole requestedRole = userRequest.getRole();

        // LIBRARIAN ne peut créer que READER
        if (currentRole.equals("ROLE_LIBRARIAN")
                && requestedRole != UserRole.READER) {

            throw new AccessDeniedException(
                    "A LIBRARIAN can only create readers"
            );
        }

        // ADMIN peut créer READER ou LIBRARIAN
        if (currentRole.equals("ROLE_ADMIN")
                && requestedRole == UserRole.ADMIN) {

            throw new AccessDeniedException(
                    "An admin cannot create another admin"
            );
        }
        
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new RuntimeException(ApiMessages.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
            .firstName(userRequest.getFirstName())
            .lastName(userRequest.getLastName())
            .email(userRequest.getEmail())
            .phone(userRequest.getPhone())
            .address(userRequest.getAddress())
            .role(userRequest.getRole())
            .password(passwordEncoder.encode(userRequest.getPassword()))
            .build();

        userRepository.save(user);
        return toUserRespose(user);
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream()
            .map(this::toUserRespose)
            .toList();
    }

    public UserResponse getUserByEmail(String email){
        User user = userRepository.findByEmail(email).get();
        if(user.getEmail().isEmpty()){
            throw new RuntimeException(ApiMessages.EMAIL_DOES_NOT_EXIST);
        }
        return toUserRespose(user);
    }

    public UserResponse getUserById(UUID id){
        User user = userRepository.findById(id).get();
        if(user.getEmail().isEmpty()){
            throw new RuntimeException(ApiMessages.ID_DOES_NOT_EXIST);
        }
        return toUserRespose(user);
    }

    public void deleteUser(UUID id){
        if(userRepository.findById(id).isEmpty()){
            throw new RuntimeException(ApiMessages.ID_DOES_NOT_EXIST);
        }
        userRepository.deleteById(id);
    }


    public UserResponse updateUser(UUID id, UserUpdateRequest userRequest){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ApiMessages.userNotFoundWithId(id)));

        if(user.getEmail().isEmpty()){
            throw new RuntimeException(ApiMessages.ID_DOES_NOT_EXIST);
        }

        if(userRequest.getFirstName() != null && !userRequest.getFirstName().isEmpty()){
            user.setFirstName(userRequest.getFirstName());
        }

        if(userRequest.getLastName() != null && !userRequest.getLastName().isEmpty()){
            user.setLastName(userRequest.getLastName());
        }

        // if(userRequest.getEmail() != null && !userRequest.getEmail().isEmpty()){
        //     user.setEmail(userRequest.getEmail());
        // }
        
        if(userRequest.getPhone() != null && !userRequest.getPhone().isEmpty()){
            user.setPhone(userRequest.getPhone());
        }

        if(userRequest.getAddress() != null && !userRequest.getAddress().isEmpty()){
            user.setAddress(userRequest.getAddress());
        }

        // if(userRequest.getStatus() != null){
        //     user.setStatus(userRequest.getStatus());
        // }

        // if(userRequest.getRole() != null){
        //     user.setRole(userRequest.getRole());
        // }

        userRepository.save(user);
        return toUserRespose(user);
    }

    public List<UserResponse> getAllReaders() {

        return userRepository.findByRole(UserRole.READER)
                .stream()
                .map(this::toUserRespose)
                .toList();
    }

    public void updateRole(Authentication authentication, UserRoleUpdate userRoleUpdate){
        String currentRole = authentication.getAuthorities()
            .iterator()
            .next()
            .getAuthority();

        User user = userRepository.findById(userRoleUpdate.getUserId()).orElseThrow();
        
        // Seul l'admin peut changer le role d'un user
        if (currentRole.equals("ROLE_ADMIN")) {

            user.setRole(userRoleUpdate.getUserRole());
            userRepository.save(user);
        }else{
            throw new AccessDeniedException(
                    "You can't do it"
            );
        }
    }

    public void updatePassword(Authentication authentication, UserPasswordUpdate userPasswordUpdate, UUID userId){
        User currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        if (currentUser.getId().equals(userId)) {

            if(userPasswordUpdate.getCnouveaupwd().equals(userPasswordUpdate.getNouveaupwd())){
                user.setPassword(userPasswordUpdate.getNouveaupwd());
                userRepository.save(user);
            }else{
                throw new RuntimeException("Password confirm incorrect");
            }

        }else{
            throw new AccessDeniedException(
                    "You can't do it"
            );
        }
    }

}
