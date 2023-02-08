package com.sparta.sbug.user.service;

import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    String signup(SignUpRequestDto requestDto);

    String login(LoginRequestDto requestDto);

    String unregister(User user);

    Optional<User> getUser(String email);

    void update(User user, UserUpdateDto dto);

    List<User> getUsers();

    User getUserById(Long userId);
}
