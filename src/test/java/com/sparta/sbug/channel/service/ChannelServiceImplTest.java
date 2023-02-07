package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.user.entity.User;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ChannelServiceImplTest {
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    ChannelServiceImpl channelService;
    static User userSample = User.builder().email("email").password("password").nickname("nickname").build();
    static User userSample2 = User.builder().email("email2").password("password2").nickname("nickname2").build();
    static Channel channelSample = Channel.builder().channelName("channelName").user(userSample).adminEmail(userSample.getEmail()).build();

    @Test
    void createChannel() {
        Channel save = channelRepository.save(channelSample);
        Channel byId = channelRepository.findById(save.getId()).orElseThrow(
                () -> new IllegalArgumentException("없음")
        );
        assertEquals(save.getId(), byId.getId());
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
    }
}