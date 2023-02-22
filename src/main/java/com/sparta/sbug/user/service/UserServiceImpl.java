package com.sparta.sbug.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.entity.QChannel;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserResponseDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import com.sparta.sbug.userchannel.enttiy.QUserChannel;
import com.sparta.sbug.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service

// springframework transaction
@Transactional
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    @CacheEvict(cacheNames = CacheNames.ALLUSERS)
    public void signup(SignUpRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickName = requestDto.getNickname();

        Optional<User> found = userRepository.findByEmailAndInUseIsTrue(email);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = User.builder()
                .email(email)
                .nickname(nickName)
                .password(password)
                .build();

        userRepository.save(user);
    }

    @Override
    public UserResponseDto login(LoginRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        User user = userRepository.findByEmailAndInUseIsTrue(email).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을수 없습니다.")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("사용자를 찾을수 없습니다.");
        }
        
        return UserResponseDto.of(user);
    }


    @Override
    @CacheEvict(cacheNames = CacheNames.ALLUSERS)
    public void unregister(User user) {
        userRepository.deleteByEmail(user.getEmail());
    }

    @Override
    @Cacheable(cacheNames = CacheNames.USER, key = "#email")
    public User getUser(String email) {
        return userRepository.findByEmailAndInUseIsTrue(email).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = CacheNames.ALLUSERS),
        @CacheEvict(cacheNames = CacheNames.USER, key = "#user.id")})
    public void updateNickname(User user, UserUpdateDto.Nickname dto) {
        User user1 = getUserById(user.getId());

        user1.setNickname(dto.getNickname());
    }

    @Override
    @Transactional
    public void changePassword(User user, UserUpdateDto.Password dto) {
        User user1 = getUserById(user.getId());

        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        user1.setPassword(encodedPassword);
    }

    @Override
    @Cacheable(cacheNames = CacheNames.ALLUSERS)
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = CacheNames.USER, key = "#user.id")
    public UserResponseDto myPage(User user) {
        User user1 = userRepository.findById(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다")
        );
        return UserResponseDto.of(user1);
    }
    @Override
    @Cacheable(cacheNames = CacheNames.USER, key = "#id")
    public UserResponseDto getUser(Long id){
        User findUser = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("선택한 유저가 없습니다.")
        );
        return UserResponseDto.of(findUser);
    }


    @Override
    public List<ChannelResponseDto> getMyChannels(User user){
        var qChannel = QChannel.channel;
        var qUserChannel = QUserChannel.userChannel;

        List<Channel> fetch = queryFactory
                .select(qChannel)
                .from(qUserChannel)
                .where(qUserChannel.user.id.eq(user.getId()))
                .fetch();

        return fetch.stream().map(ChannelResponseDto::of).collect(Collectors.toList());
    }
    // 요청한 유자가 가진 채널의 목록을 조회

    @Override
    @Cacheable(cacheNames = CacheNames.USER, key = "#userId")
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("선택한 유저가 없습니다.")
        );
    }
    
}
