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
class UserChannelUpperLayerServiceImplTest {

    // Repository
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    UserChannelRepository userChannelRepository;

    @Test
    @DisplayName("Channel Upper Channel Service : Get Channels By Channel ID")
    @Transactional
    void getChannelsByUserId() {

    }

}