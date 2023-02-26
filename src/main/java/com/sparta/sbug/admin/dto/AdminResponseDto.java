package com.sparta.sbug.admin.dto;

import com.sparta.sbug.admin.entity.Admin;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 관리자 로그인 요청 DTO
 */
// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminResponseDto {

    Long userId;

    String email;

    private AdminResponseDto(Admin admin) {
        this.userId = admin.getId();
        this.email = admin.getEmail();
    }

    public static AdminResponseDto of(Admin admin){
        return new AdminResponseDto(admin);
    }
}
