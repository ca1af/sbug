package com.sparta.sbug.admin.service;

import com.sparta.sbug.admin.dto.AdminResponseDto;

public interface AdminService {

    /**
     * 관리자 회원가입
     *
     * @param email    이메일
     * @param password 비밀번호
     * @param nickname 닉네임
     */
    void signUpAdmin(String email, String password, String nickname);

    /**
     * 관리자 로그인
     *
     * @param email    이메일
     * @param password 패스워드
     * @return AdminResponseDto
     */
    AdminResponseDto loginAdmin(String email, String password);
}
