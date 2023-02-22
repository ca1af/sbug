package com.sparta.sbug.user.controller;

import com.sparta.sbug.aws.service.S3Service;
import com.sparta.sbug.security.dto.TokenResponseDto;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.List;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@CrossOrigin
public class UserController {
    private final UserServiceImpl userService;
    private final JwtProvider jwtProvider;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.credentials.access-key}")
    private String ACCESS_KEY;
    @Value("${cloud.aws.credentials.secret-key}")
    private String SECRET_KEY;


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
        log.info("[GET] /api/users/my-page");
        User user = userDetails.getUser();
        UserResponseDto responseDto = UserResponseDto.of(user);
        S3Presigner preSigner = getPreSigner();
        responseDto.setProfileImageUrl(s3Service.getObjectPreSignedUrl(bucketName, user.getProfileImage(), preSigner));
        preSigner.close();
        return responseDto;
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
     * @return TokenResponseDto
     */
    @GetMapping("/account/reissue")
    public TokenResponseDto reissue(@AuthenticationPrincipal UserDetailsImpl accountDetails) {
        UserResponseDto accountResponse = UserResponseDto.of(accountDetails.getUser());
        return jwtProvider.reissueAtk(accountResponse);
    }

    public S3Presigner getPreSigner() {
        AwsCredentialsProvider awsCredentialsProvider;
        String accessKey = ACCESS_KEY;
        String secretKey = SECRET_KEY;

        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        awsCredentialsProvider = StaticCredentialsProvider.create(awsBasicCredentials);

        Region region = Region.AP_NORTHEAST_2;
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @PostMapping("/api/users/test")
    public String updateProfileImage(@RequestBody String key, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("[GET] /api/users/test");
        S3Presigner preSigner = getPreSigner();
        String url = s3Service.putObjectPreSignedUrl(bucketName, key, preSigner);
        preSigner.close();
        return url;
    }
}