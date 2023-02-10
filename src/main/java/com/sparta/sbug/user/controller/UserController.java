package com.sparta.sbug.user.controller;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.security.dto.JwtDto;
import com.sparta.sbug.security.jwt.JwtUtil;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/api/user/sign-up")
    public String signup(@RequestBody SignUpRequestDto requestDto){
        return userService.signup(requestDto);
    }
    @PostMapping("/api/user/login")
    public String login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        JwtDto token = userService.login(requestDto);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token.getAccessToken());
        return "Success";
    }
    @DeleteMapping("/api/user/unregister")
    public String unregister(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.unregister(userDetails.getUser());
    }
    @PutMapping("/api/user/update")
    public String update(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserUpdateDto dto){
        userService.update(userDetails.getUser(),dto);
        return "updated";
    }
    @GetMapping("/api/users")
    public List<UserResponseDto> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("/api/user/mypage")
    public UserResponseDto myPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.myPage(userDetails.getUser());
    }
    @GetMapping("/api/user/channel")
    public List<ChannelResponseDto> getMyChannels(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userService.getMyChannels(userDetails.getUser());
    }
}