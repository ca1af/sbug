package com.sparta.sbug.channel.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.entity.QChannel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.QThread;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.entity.UserRole;
import com.sparta.sbug.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ChannelServiceImplTest {
    @Autowired
    ThreadRepository threadRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    private final PageDto pageDto = PageDto.builder().currentPage(1).size(5).sortBy("createdAt").build();

    @BeforeEach
    @Transactional
    public void createUser() {
        User user1 = User.builder().email("user1").password("password1").nickname("뽀로로").build();
        user1.setUserRole(UserRole.USER);
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder().email("user2").password("password2").nickname("루피").build();
        user2.setUserRole(UserRole.USER);
        User savedUser2 = userRepository.save(user2);

        User user3 = User.builder().email("user3").password("password3").nickname("펭구").build();
        user2.setUserRole(UserRole.USER);
        User savedUser3 = userRepository.save(user2);

        Channel channel = Channel.builder()
                .user(savedUser1)
                .adminEmail(savedUser1.getEmail())
                .channelName("channel").build();

        Channel channel2 = Channel.builder()
                .user(savedUser1)
                .adminEmail(savedUser1.getEmail())
                .channelName("channel2").build();

        Channel savedChannel = channelRepository.save(channel);
        Channel savedChannel2 = channelRepository.save(channel2);

        savedUser1.addChannel(savedChannel);
        savedUser2.addChannel(savedChannel);
        savedUser3.addChannel(savedChannel2);

        Thread thread = new Thread(savedChannel, savedUser1, "안녕하세요");
        Thread thread2 = new Thread(savedChannel, savedUser1, "안녕하세요2");

        Thread savedThread = threadRepository.save(thread);
        Thread savedThread2 = threadRepository.save(thread2);

        savedChannel.addThread(savedThread);
        savedChannel2.addThread(savedThread2);

    }

    @Test
    void createChannel() {

    }

    @Test
    void inviteUser() {

    }

    @Test
    void updateChannelName() {
    }

    @Test
    void deleteChannel() {
    }

    @Test
    void getThreads() {
        Channel channel = channelRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("없음")
        );

        QThread qThread = QThread.thread;
        QChannel qChannel = QChannel.channel;

        List<Thread> fetch = queryFactory
                .select(qThread)
                .from(qThread)
                .join(qThread.channel)
                .on(qThread.channel.channelName.eq(channel.getChannelName()))
                .where(qThread.channel.channelName.eq(channel.getChannelName()))
                .fetch();

        List<ThreadResponseDto> collect = fetch.stream().map(ThreadResponseDto::of).toList();
        List<String> collect1 = collect.stream().map(ThreadResponseDto::getContent).toList();
        System.out.println("collect1 = " + collect1);
    }
}