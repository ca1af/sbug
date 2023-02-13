package com.sparta.sbug.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
            message = "이메일 형식으로 입력 해 주세요.")
    private String email;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
            message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자로 이루어져야 합니다.")
    private String password;
    private String nickname;
}
