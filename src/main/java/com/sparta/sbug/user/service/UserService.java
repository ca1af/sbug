package com.sparta.sbug.user.service;

import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;

public interface UserService {
    String signup(SignUpRequestDto requestDto);

    String login(LoginRequestDto requestDto);

    String unregister(User user);

    String myPage();

    void update(User user, UserUpdateDto dto);
}
