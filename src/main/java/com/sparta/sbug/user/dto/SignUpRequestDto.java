package com.sparta.sbug.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원가입 요청 DTO
 */
// lombok
@Getter
@AllArgsConstructor
public class SignUpRequestDto {
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
            message = "이메일 형식으로 입력 해 주세요.")
    private String email;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
            message = "비밀번호는 8자 이상 20자 이하, 영문자, 숫자, 특수문자로 이루어져야 합니다.")
    private String password;
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "닉네임은 완성형 한글이나 영문, 숫자로 이루어져야 하고, 2~10글자여야 합니다.")
    private String nickname;

    /**
     * ALLUSERS cache의 key를 생성하기 위한 method
     */
    @Override
    public String toString() {
        return "ALLUSERS_CACHE_KEY";
    }
}
