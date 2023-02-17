package com.sparta.sbug.admin.service;

import com.sparta.sbug.admin.dto.AdminResponseDto;
import com.sparta.sbug.admin.entity.Admin;
import com.sparta.sbug.admin.respository.AdminRepository;
import com.sparta.sbug.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final AdminRepository adminRepository;
    @Override
    @Transactional(readOnly = true)
    public AdminResponseDto adminLogin(LoginRequestDto requestDto){
        String password = requestDto.getPassword();

        Admin admin = adminRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("관리자를 찾을 수 없습니다.")
        );

        if (!password.equals(admin.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return AdminResponseDto.of(admin);
    }
}
