package com.sparta.sbug.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.sbug.security.dto.TokenResponse;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserServiceImpl userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/api/users/sign-up")
    public String signup(@RequestBody @Valid SignUpRequestDto requestDto) {
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
    public String update(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserUpdateDto dto) {
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

//    @GetMapping("/api/users/channels")
//    public List<ChannelResponseDto> getMyChannels(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return userService.getMyChannels(userDetails.getUser());
//    }
    @PostMapping("/api/users/logout")
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails){
        jwtProvider.deleteRtk(userDetails.getUser().getEmail());
        // 이 부분은 redis 에 저장되어 있는 리프레쉬 토큰을 삭제합니다.
        // 프론트에서 로그아웃 API를 받으면 사용자의 헤더에 있는 모든 토큰을 지워야 합니다.
        // 백엔드에서 처리하려면 요청하는 사용자의 헤더에 있는 값을 set해야 하는데, 자연스럽지 않은 듯 합니다.
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