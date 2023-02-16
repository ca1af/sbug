package com.sparta.sbug.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class LoginRequestDto {
    private String email;
    private String password;
}
