package com.sparta.sbug.user.dto;

import com.sparta.sbug.user.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 정보 응답 DTO
 */
@Getter
public class UserResponseDto {
    Long userId;
    String email;
    String nickname;
    @Setter
    String profileImageUrl;

    /**
     * 생성자
     */
    private UserResponseDto(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }

    /**
     * 유저 객체를 DTO에 담아 반환해줍니다.
     *
     * @param user 유저 객체
     * @return UserResponseDto
     */
    public static UserResponseDto of(User user) {
        return new UserResponseDto(user);
    }
}
