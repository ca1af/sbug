package com.sparta.sbug.user.service;

import com.sparta.sbug.security.jwt.JwtUtil;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Override
    public String signup(SignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickName = requestDto.getNickname();

        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = User.builder()
                .email(email)
                .nickname(nickName)
                .password(password)
                .build();

        userRepository.save(user);
        return "login-page";
    }

    @Override
    public String login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new SecurityException("사용자를 찾을수 없습니다.")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SecurityException("사용자를 찾을수 없습니다.");
        }
        jwtUtil.createToken(user.getEmail(), user.getUserRole());

        return "homepage";
    }

    @Override
    public String unregister(User user) {
        userRepository.deleteByEmail(user.getEmail());
        return "home";
    }

    @Override
    public String myPage() {
        return null;
    }

    @Override
    public void update(User user, UserUpdateDto dto) {
        user.updateUser(dto.getNickname(), dto.getPassword());
    }
}
