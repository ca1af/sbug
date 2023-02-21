package com.sparta.sbug.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;

/**
 * 유저 정보 수정 DTO
 */
@Data
public class UserUpdateDto {
    // 프론트에서, 수정 갈 때 인증 객체의 네임과 패스워드 받아서 자동으로 채워주는 형식이 자연스럽지 않을까? <

    @Getter
    public static class Nickname {
        @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$", message = "닉네임은 영문이나 숫자로 이루어져야 하고, 4~10글자여야 합니다.")
        private String nickname;
    }

    @Getter
    public static class Password {
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
                message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자의 혼합으로 이루어져야 합니다.")
        private String password;
    }
}
