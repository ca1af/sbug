package com.sparta.sbug.channel.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.QThread;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.entity.UserRole;
import com.sparta.sbug.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        List<Thread> fetch = queryFactory
                .selectFrom(qThread)
                .join(qThread.channel)
                .on(qThread.channel.id.eq(channel.getId()))
                .where(qThread.channel.id.eq(channel.getId()))
                .fetch();

        List<ThreadResponseDto> collect = fetch.stream().map(ThreadResponseDto::of).toList();
        List<String> collect1 = collect.stream().map(ThreadResponseDto::getContent).toList();
        System.out.println("collect1 = " + collect1);
    }
}