package com.sparta.sbug.user.controller;

import com.sparta.sbug.aop.ExeTimer;
import com.sparta.sbug.security.dto.TokenResponseDto;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.service.UserServiceImpl;
import com.sparta.sbug.userchannel.service.UserChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    private final UserServiceImpl userService;
    private final UserChannelService userChannelService;

    private final JwtProvider jwtProvider;


    /**
     * 회원가입
     * [POST] /api/users/sign-up
     *
     * @param requestDto 회원가입 요청 DTO (이메일, 비밀번호, 닉네임)
     */
    @PostMapping("/users/sign-up")
    public void signUp(@RequestBody @Valid SignUpRequestDto requestDto) {
        log.info("[POST] /api/users/sign-up");

        userService.signUp(requestDto);
    }

    /**
     * 로그인
     * [POST] /api/users/login
     *
     * @param requestDto 로그인 요청 DTO (이메일, 비밀번호)
     * @return TokenResponseDto 토큰이 담긴 응답 DTO
     */
    @PostMapping("/users/login")
    @ExeTimer
    public TokenResponseDto login(@RequestBody LoginRequestDto requestDto) {
        log.info("[POST] /api/users/login");

        UserResponseDto responseDto = userService.login(requestDto);
        return jwtProvider.createTokensByLogin(responseDto);
    }

    /**
     * 로그아웃
     * redis 에 저장되어 있는 리프레쉬 토큰을 삭제합니다.
     * [POST] /api/users/logout
     *
     * @param userDetails 요청자
     */
    @PostMapping("/users/logout")
    public void logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("[POST] /api/users/logout");

        jwtProvider.deleteRtk(userDetails.getUser().getEmail());
    }

    /**
     * 모든 유저의 리스트를 조회
     * [GET] /api/users
     *
     * @return List&lt;UserResponseDto&gt;
     */
    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        log.info("[GET] /api/users");

        return userService.getUsers();
    }

    /**
     * 요청자 자신의 유저 정보를 조회
     * [GET] /api/users/my-page
     *
     * @param userDetails 요청자
     * @return UserResponseDto
     */
    @GetMapping("/users/my-page")
    public UserResponseDto myPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("[GET] /api/users/my-page");
        return userService.getMyPage(userDetails.getUser());
    }

    /**
     * 대상 사용자의 유저 정보를 조회
     * [GET] /api/users/{userId}
     *
     * @param userId 대상 사용자
     * @return UserResponseDto
     */
    @GetMapping("/users/{userId}")
    public UserResponseDto getUser(@PathVariable Long userId) {
        log.info("[GET] /api/users/" + userId);
        return userService.getUser(userId);
    }

    /**
     * 회원 정보 수정 ( 닉네임 )
     * [PATCH] /api/users/nickname
     *
     * @param userDetails 요청자
     * @param dto         수정 요청 DTO (닉네임 )
     */
    @PatchMapping("/users/nickname")
    public void updateNickname(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserUpdateDto.Nickname dto) {
        log.info("[PATCH] /api/users/nickname");

        userService.updateNickname(userDetails.getUser(), dto);
    }

    /**
     * 회원 정보 수정 ( 비밀번호 )
     * [PATCH] /api/users/password
     *
     * @param userDetails 요청자
     * @param dto         수정 요청 DTO ( 비밀번호 )
     */
    @PatchMapping("/users/password")
    public void changePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid UserUpdateDto.Password dto) {
        log.info("[PATCH] /api/users/password");

        userService.changePassword(userDetails.getUser(), dto);
    }

    /**
     * 회원 정보 수정 ( 프로필 이미지 )
     *
     * @param key         수정 요청 파일 이름
     * @param userDetails 요청자
     * @return String (Pre-signed URL)
     */
    @PatchMapping("/users/image")
    public String updateProfileImage(@RequestBody String key, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("[GET] /api/users/test");

        return userService.changeProfileImage(userDetails.getUser(), key);
    }

    /**
     * 회원탈퇴
     * [PUT] /api/users
     *
     * @param userDetails 요청자
     */
    @PutMapping("/users")
    public void unregister(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("[PUT] /api/users");

        userChannelService.disableUser(userDetails.getUser());
    }

    /**
     * 토큰 재발급(reissue)
     * [GET] /api/users/reissue
     *
     * @param accountDetails 재발급 대상 정보
     * @return TokenResponseDto
     */
    @GetMapping("/users/reissue")
    public TokenResponseDto reissue(@AuthenticationPrincipal UserDetailsImpl accountDetails) {
        UserResponseDto accountResponse = UserResponseDto.of(accountDetails.getUser());
        return jwtProvider.reissueAtk(accountResponse);
    }
}
