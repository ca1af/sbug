package com.sparta.sbug.user.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
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

    List<UserResponseDto> getUsers();
    
    UserResponseDto myPage(User user);
    
    UserResponseDto getUser(Long id);
    
    List<ChannelResponseDto> getMyChannels(User user);
    
    User getUserById(Long userId);
}
