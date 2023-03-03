package com.sparta.sbug.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.aws.service.S3Service;
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
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JPAQueryFactory queryFactory;

    // for S3
    private final S3Service s3Service;

    @Override
    @Transactional
    public void signUp(SignUpRequestDto requestDto) {
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
        user.setProfileImage("default.png");

        userRepository.save(user);
    }

    @Override
    @Transactional
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
    public void unregister(User user) {
        userRepository.disableInUseByEmail(user.getEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsers() {
        return userRepository.findAll().stream().map(UserResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getMyPage(User user) {
        return getUserResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUser(String email) {
        User user = getUserByEmail(email);
        return getUserResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> getMyChannels(User user) {
        var qChannel = QChannel.channel;
        var qUserChannel = QUserChannel.userChannel;

        List<Channel> fetch = queryFactory
                .select(qChannel)
                .from(qUserChannel)
                .where(qUserChannel.user.id.eq(user.getId()))
                .fetch();

        return fetch.stream().map(ChannelResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Transactional
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
    @Transactional
    public String changeProfileImage(User user, String key) {
        String uniqueKey = key + user.getEmail();

        S3Presigner preSigner = s3Service.getPreSigner();
        String url = s3Service.putObjectPreSignedUrl(s3Service.bucketName, uniqueKey, preSigner);
        preSigner.close();

        user.setProfileImage(uniqueKey);
        userRepository.save(user);
        return url;
    }

    @Override
    @Transactional
    public void AddOrSubtractTemperatureByConfidence(User user, String confidence) {
        Float temp = user.getTemperature();
        if (confidence.equals("positive")) {
            temp += 0.1f;
        } else if (confidence.equals("negative")) {
            temp -= 0.1f;
        }
        user.setTemperature(temp);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndInUseIsTrue(email).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("선택한 유저가 없습니다.")
        );
    }

    @Override
    public UserResponseDto getUserResponseDto(User user) {
        UserResponseDto responseDto = UserResponseDto.of(user);
        S3Presigner preSigner = s3Service.getPreSigner();
        responseDto.setProfileImageUrl(s3Service.getObjectPreSignedUrl(s3Service.bucketName, user.getProfileImage(), preSigner));
        preSigner.close();
        return responseDto;
    }
}
