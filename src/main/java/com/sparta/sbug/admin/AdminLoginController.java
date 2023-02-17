package com.sparta.sbug.admin;

import com.sparta.sbug.admin.dto.AdminResponseDto;
import com.sparta.sbug.admin.service.AdminService;
import com.sparta.sbug.security.dto.TokenResponseDto;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminLoginController {
    private final AdminService adminService;
    private final JwtProvider jwtProvider;

    @PostMapping("/api/admins/login")
    public TokenResponseDto adminLogin(@RequestBody LoginRequestDto requestDto){
        AdminResponseDto responseDto = adminService.adminLogin(requestDto);
        return jwtProvider.createTokenAdmin(responseDto.getEmail());
    }
}
