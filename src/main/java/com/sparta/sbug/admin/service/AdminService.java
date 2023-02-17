package com.sparta.sbug.admin.service;

import com.sparta.sbug.admin.dto.AdminResponseDto;
import com.sparta.sbug.user.dto.LoginRequestDto;

public interface AdminService {

    public AdminResponseDto adminLogin(LoginRequestDto requestDto);
}
