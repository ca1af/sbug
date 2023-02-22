package com.sparta.sbug.user.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 회원가입
     *
     * @param requestDto 회원가입 유저 정보
     */
    void signup(SignUpRequestDto requestDto);

    /**
     * 로그인
     *
     * @param requestDto 로그인 유저 정보
     * @return UserResponseDto
     */
    UserResponseDto login(LoginRequestDto requestDto);

    /**
     * 회원탈퇴
     *
     * @param user 사용자
     */
    void unregister(User user);

    /**
     * 이메일로 사용자 정보 조회
     *
     * @param email 이메일
     * @return User
     */
    User getUser(String email);

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
     * 전체 사용자 조회
     *
     * @return List&lt;UserResponseDto&gt;
     */
    List<UserResponseDto> getUsers();

    /**
     * 내 사용자 정보 조회
     *
     * @param user 사용자
     * @return UserResponseDto
     */
    UserResponseDto myPage(User user);

    /**
     * 대상 사용자 정보 조회
     *
     * @param id 대상 사용자 ID
     * @return UserResponseDto
     */
    UserResponseDto getUser(Long id);

    /**
     * 사용자가 속한 채널들의 정보를 조회
     *
     * @param user 요청자
     * @return List&lt;ChannelResponseDto&gt;
     */
    List<ChannelResponseDto> getMyChannels(User user);

    /**
     * 사용자의 아이디로 사용자 엔티티 조회
     *
     * @param userId 사용자 ID
     * @return User
     */
    User getUserById(Long userId);
}
