package com.sparta.sbug.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;

/**
 * 유저 정보 수정 DTO
 */
@Data
public class UserUpdateDto {
    @Getter
    public static class Nickname {
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 완성형 한글이나 영문, 숫자로 이루어져야 하고, 2~10글자여야 합니다.")
        private String nickname;
    }

    @Getter
    public static class Password {
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
                message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자의 혼합으로 이루어져야 합니다.")
        private String password;
    }
}
