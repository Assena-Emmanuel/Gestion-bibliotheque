package com.biblio.app.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor
public class UserPasswordUpdate {
    private String nouveaupwd;
    private String cnouveaupwd;
}
