package com.biblio.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
