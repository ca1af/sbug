package com.sparta.sbug.user.dto;

import com.sparta.sbug.user.entity.User;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class UserResponseDto {
    Long userId;
    String email;
    String nickname;

    private UserResponseDto(User user){
        this.userId = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }

    public static UserResponseDto of(User user){
        return new UserResponseDto(user);
    }
}
