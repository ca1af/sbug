package com.sparta.sbug.user.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
}
