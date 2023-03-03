package com.sparta.sbug.user.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    /**
     * 회원가입
     *
     * @param requestDto 회원가입 유저 정보
     */
    void signUp(SignUpRequestDto requestDto);

    /**
     * 로그인
     *
     * @param requestDto 로그인 유저 정보
     * @return UserResponseDto
     */
    UserResponseDto login(LoginRequestDto requestDto);

    /**
     * 전체 사용자 조회
     *
     * @return List&lt;UserResponseDto&gt;
     */
    List<UserResponseDto> getUsers();

    /**
     * 내 사용자 정보 조회
     *
     * @param user 요청자 (=나)
     * @return UserResponseDto
     */
    UserResponseDto getMyPage(User user);

    /**
     * 대상 사용자 정보 조회
     *
     * @param email 대상 사용자 email
     * @return UserResponseDto
     */
    UserResponseDto getUser(String email);

    /**
     * 사용자가 속한 채널들의 정보를 조회
     *
     * @param user 요청자
     * @return List&lt;ChannelResponseDto&gt;
     */
    List<ChannelResponseDto> getMyChannels(User user);

    /**
     * 사용자 닉네임 수정
     *
     * @param user 사용자
     * @param dto  수정될 닉네임
     */
    void updateNickname(User user, UserUpdateDto.Nickname dto);

    /**
     * 사용자 비밀번호 변경
     *
     * @param user 사용자
     * @param dto  변경될 비밀번호
     */
    void changePassword(User user, UserUpdateDto.Password dto);

    /**
     * 사용자 프로필 이미지 변경
     *
     * @param user 사용자
     * @param key  변경될 프로필 이미지 이름
     * @return
     */
    String changeProfileImage(User user, String key);

    /**
     * 회원탈퇴
     *
     * @param user 사용자
     */
    void unregister(User user);

    @Transactional
    void AddOrSubtractTemperatureByConfidence(User user, String confidence);

    /**
     * 이메일로 사용자 엔터티  조회
     *
     * @param email 이메일
     * @return User
     */
    User getUserByEmail(String email);


    /**
     * 사용자의 아이디로 사용자 엔터티 조회
     *
     * @param userId 사용자 ID
     * @return User
     */
    User getUserById(Long userId);

    /**
     * 유저 응답 DTO를 생성
     *
     * @param user 대상 유저 엔터티
     * @return UserResponseDto
     */
    UserResponseDto getUserResponseDto(User user);
}
