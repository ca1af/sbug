package com.sparta.sbug.user.controller;

import com.sparta.sbug.security.dto.TokenResponseDto;
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

// lombok
@RequiredArgsConstructor

// springframework web bind
@RestController
@CrossOrigin
public class UserController {
    private final UserServiceImpl userService;
    private final JwtProvider jwtProvider;

    /**
     * 회원가입
     * [POST] /api/users/sign-up
     *
     * @param requestDto 회원가입 요청 DTO (이메일, 비밀번호, 닉네임)
     */
    @PostMapping("/api/users/sign-up")
    public void signup(@RequestBody @Valid SignUpRequestDto requestDto) {
        userService.signup(requestDto);
    }

    /**
     * 로그인
     * [POST] /api/users/login
     *
     * @param requestDto 로그인 요청 DTO (이메일, 비밀번호)
     * @return TokenResponseDto 토큰이 담긴 응답 DTO
     */
    @PostMapping("/api/users/login")
    public TokenResponseDto login(@RequestBody LoginRequestDto requestDto) {
        UserResponseDto responseDto = userService.login(requestDto);
        return jwtProvider.createTokensByLogin(responseDto);
    }

    /**
     * 회원탈퇴
     * [DELETE] /api/users
     *
     * @param userDetails 요청자
     */
    @DeleteMapping("/api/users")
    public void unregister(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.unregister(userDetails.getUser());
    }

    /**
     * 회원 정보 수정
     * [PUT] /api/users
     *
     * @param userDetails 요청자
     * @param dto         수정 요청 DTO (닉네임, 비밀번호)
     */
    @PutMapping("/api/users")
    public void update(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserUpdateDto dto) {
        userService.update(userDetails.getUser(), dto);
    }

    /**
     * 모든 유저의 리스트를 조회
     * [GET] /api/users
     *
     * @return List&lt;UserResponseDto&gt;
     */
    @GetMapping("/api/users")
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    /**
     * 요청자 자신의 유저 정보를 조회
     * [GET] /api/users/my-page
     *
     * @param userDetails 요청자
     * @return UserResponseDto
     */
    @GetMapping("/api/users/my-page")
    public UserResponseDto myPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.myPage(userDetails.getUser());
    }

    /**
     * 로그아웃
     * redis 에 저장되어 있는 리프레쉬 토큰을 삭제합니다.
     * [POST] /api/users/logout
     *
     * @param userDetails 요청자
     */
    @PostMapping("/api/users/logout")
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        jwtProvider.deleteRtk(userDetails.getUser().getEmail());
    }

    /**
     * 토큰 재발급(reissue)
     * [GET] /account/reissue
     *
     * @param accountDetails 재발급 대상 정보
     * @param response       HTTP 서블릿 응답
     * @return TokenResponseDto
     */
    @GetMapping("/account/reissue")
    public TokenResponseDto reissue(@AuthenticationPrincipal UserDetailsImpl accountDetails, HttpServletResponse response) {
        UserResponseDto accountResponse = UserResponseDto.of(accountDetails.getUser());
        TokenResponseDto tokenResponseDto = jwtProvider.reissueAtk(accountResponse);
        response.setHeader("Authorization", tokenResponseDto.getAtk());
        return tokenResponseDto;
    }
}