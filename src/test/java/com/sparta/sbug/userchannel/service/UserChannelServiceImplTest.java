package com.sparta.sbug.userchannel.service;

import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.user.repository.UserRepository;
import com.sparta.sbug.userchannel.repository.UserChannelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserChannelServiceImplTest {

    // Repository
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    UserChannelRepository userChannelRepository;

    // Service
    @Autowired
    UserChannelService userChannelService;

    @Test
    @DisplayName("UserChannel Service : Get User-Channels By User ID")
    @Transactional
    void getUserChannelsByUserId() {

    }

    @Test
    @DisplayName("UserChannel Service : Create User-Channel ")
    @Transactional
    void createUserChannel() {

    }

    @Test
    @DisplayName("UserChannel Service : Delete User-Channels About Channel")
    @Transactional
    void deleteUserChannelsAboutChannel() {

    }

    @Test
    @DisplayName("UserChannel Service : Delete User-Channels About Channel")
    @Transactional
    void deleteUserChannelsAboutUser() {

    }

    @Test
    @DisplayName("UserChannel Service : Delete User-Channels By User And Channel")
    @Transactional
    void deleteUserChannelByUserAndChannel() {

    }

}