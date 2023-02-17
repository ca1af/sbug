package com.sparta.sbug.admin.dto;

import com.sparta.sbug.admin.entity.Admin;
import com.sparta.sbug.user.entity.User;
import lombok.Getter;

@Getter
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
