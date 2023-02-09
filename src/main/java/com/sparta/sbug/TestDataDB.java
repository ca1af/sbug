package com.sparta.sbug;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.entity.UserRole;
import com.sparta.sbug.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDataDB {
    private final InitService initService;

    @PostConstruct
    public void initialize() {
        initService.init();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final ThreadRepository threadRepository;

        private final UserRepository userRepository;

        private final ChannelRepository channelRepository;
        private final PasswordEncoder passwordEncoder;

        private final JPAQueryFactory queryFactory;
        static final PageDto pageDto = PageDto.builder().currentPage(1).size(5).sortBy("createdAt").build();

        public void init() {
            String password1 = getEncode("password1");
            String password2 = getEncode("password2");
            String password3 = getEncode("password3");

            User user1 = User.builder().email("user1").password(password1).nickname("뽀로로").build();
            user1.setUserRole(UserRole.USER);
            User savedUser1 = userRepository.save(user1);

            User user2 = User.builder().email("user2").password(password2).nickname("루피").build();
            user2.setUserRole(UserRole.USER);
            userRepository.save(user2);

            User user3 = User.builder().email("user3").password(password3).nickname("펭구").build();
            user2.setUserRole(UserRole.USER);
            userRepository.save(user2);

            Channel channel = Channel.builder()
                    .adminEmail(savedUser1.getEmail())
                    .channelName("channel").build();

//            channel.joinChannel(savedUser1);

            Channel channel2 = Channel.builder()
                    .adminEmail(savedUser1.getEmail())
                    .channelName("channel2").build();

//            channel2.joinChannel(savedUser1);

            Channel savedChannel = channelRepository.save(channel);
            Channel savedChannel2 = channelRepository.save(channel2);


            Thread thread = new Thread(savedChannel, savedUser1, "안녕하세요");
            Thread thread2 = new Thread(savedChannel2, savedUser1, "안녕하세요2");

            Thread savedThread = threadRepository.save(thread);
            Thread savedThread2 = threadRepository.save(thread2);

            savedChannel.addThread(savedThread);
            savedChannel2.addThread(savedThread2);
        }

        private String getEncode(String password) {
            return passwordEncoder.encode(password);
        }
    }
}
