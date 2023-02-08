package com.sparta.sbug.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.entity.QChannel;
import com.sparta.sbug.security.jwt.JwtUtil;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.QUser;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final JPAQueryFactory queryFactory;
    @Override
    public String signup(SignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickName = requestDto.getNickname();

        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = User.builder()
                .email(email)
                .nickname(nickName)
                .password(password)
                .build();

        userRepository.save(user);
        return "login-page";
    }

    @Override
    public String login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new SecurityException("사용자를 찾을수 없습니다.")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new SecurityException("사용자를 찾을수 없습니다.");
        }

        return jwtUtil.createToken(user.getEmail(), user.getUserRole());
    }

    @Override
    public String unregister(User user) {
        userRepository.deleteByEmail(user.getEmail());
        return "home";
    }

    @Override
    public Optional<User> getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void update(User user, UserUpdateDto dto) {
        user.updateUser(dto.getNickname(), dto.getPassword());
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::of).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto myPage(User user) {
        User user1 = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다")
        );
        return UserResponseDto.of(user1);
    }
    @Override
    public UserResponseDto getUser(Long id){
        User findUser = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 유저가 없습니다.")
        );
        return UserResponseDto.of(findUser);
    }


    @Override
    public List<ChannelResponseDto> getMyChannels(User user){
        QChannel qChannel = QChannel.channel;
        QUser qUser = QUser.user;

        List<Channel> channels = queryFactory
                .selectFrom(qChannel)
                .join(qUser)
                .on(qChannel.user.eq(user))
                .fetch();
        return channels.stream().map(ChannelResponseDto::of).collect(Collectors.toList());
    }
    // 요청한 유자가 가진 채널의 목록을 조회
}
