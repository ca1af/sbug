package com.sparta.sbug.admin.service;

import com.sparta.sbug.admin.dto.AdminResponseDto;
import com.sparta.sbug.admin.entity.Admin;
import com.sparta.sbug.admin.respository.AdminRepository;
import com.sparta.sbug.common.exceptions.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.sbug.common.exceptions.ErrorCode.*;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public void signUpAdmin(String email, String password, String nickname) {
        if (adminRepository.existsAdminByEmail(email)) {
            throw new CustomException(DUPLICATE_ADMIN);
        }

        Admin admin = Admin.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();

        adminRepository.save(admin);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminResponseDto loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ADMIN_NOT_FOUND)
        );

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new CustomException(ADMIN_PASSWORD_NOT_MATCH);
        }

        return AdminResponseDto.of(admin);
    }
}
