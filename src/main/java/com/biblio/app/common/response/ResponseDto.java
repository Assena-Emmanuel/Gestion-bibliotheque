package com.biblio.app.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter @Builder
public class ResponseDto {
    private boolean success;
    private String message;
    private Object data;
}
