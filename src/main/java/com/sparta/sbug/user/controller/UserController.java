package com.sparta.sbug.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.security.dto.TokenResponse;
import com.sparta.sbug.security.jwt.JwtProvider;
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
    private final JwtProvider jwtProvider;

    @PostMapping("/api/users/sign-up")
    public String signup(@RequestBody SignUpRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/api/users/login")
    public TokenResponse login(@RequestBody LoginRequestDto requestDto) throws JsonProcessingException {
        UserResponseDto responseDto = userService.login(requestDto);
        return jwtProvider.createTokensByLogin(responseDto);
    }

    @DeleteMapping("/api/users")
    public String unregister(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.unregister(userDetails.getUser());
    }

    @PutMapping("/api/users")
    public String update(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserUpdateDto dto) {
        userService.update(userDetails.getUser(), dto);
        return "updated";
    }

    @GetMapping("/api/users")
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/api/users/my-page")
    public UserResponseDto myPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.myPage(userDetails.getUser());
    }

    @GetMapping("/api/users/channels")
    public List<ChannelResponseDto> getMyChannels(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getMyChannels(userDetails.getUser());
    }

    @GetMapping("/account/reissue")
    public TokenResponse reissue(@AuthenticationPrincipal UserDetailsImpl accountDetails, HttpServletResponse response)
            throws JsonProcessingException {
        UserResponseDto accountResponse = UserResponseDto.of(accountDetails.getUser());
        TokenResponse tokenResponse = jwtProvider.reissueAtk(accountResponse);
        response.setHeader("Authorization", tokenResponse.getAtk());
        return tokenResponse;
    }
}