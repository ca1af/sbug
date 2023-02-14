package com.sparta.sbug.user.dto;

import com.sparta.sbug.user.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Objects;

@Data
public class UserUpdateDto {
    @NotNull
    private String nickname;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
            message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자의 혼합으로 이루어져야 합니다.")
    private String password;
    // 프론트에서, 수정 갈 때 인증 객체의 네임과 패스워드 받아서 자동으로 채워주는 형식이 자연스럽지 않을까? <

    public UserUpdateDto(String nickname, String password) {
        this.nickname = Objects.requireNonNullElse(nickname, "");
        this.password = password;
    }
}
