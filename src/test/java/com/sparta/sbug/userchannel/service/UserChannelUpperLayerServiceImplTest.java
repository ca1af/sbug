package com.sparta.sbug.userchannel.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.upperlayerservice.UserChannelUpperLayerService;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import com.sparta.sbug.userchannel.enttiy.UserChannel;
import com.sparta.sbug.userchannel.repository.UserChannelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
class UserChannelUpperLayerServiceImplTest {

    // Repository
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    UserChannelRepository userChannelRepository;

    // Service
    @Autowired
    UserChannelUpperLayerService upperLayerService;

    @Test
    @DisplayName("Channel Upper Channel Service : Get Channels By Channel ID")
    @Transactional
    void getChannelsByUserId() {
        // give
        User user = userRepository.findByEmail("user1").orElseThrow();

        Channel channel1 = Channel.builder()
                .adminEmail(user.getEmail())
                .channelName("test channel1").build();

        Channel channel2 = Channel.builder()
                .adminEmail(user.getEmail())
                .channelName("test channel1").build();

        Channel channel3 = Channel.builder()
                .adminEmail(user.getEmail())
                .channelName("test channel1").build();

        Channel savedChannel1 = channelRepository.save(channel1);
        Channel savedChannel2 = channelRepository.save(channel2);
        Channel savedChannel3 = channelRepository.save(channel3);

        UserChannel userChannel1 = UserChannel.builder().user(user).channel(savedChannel1).build();
        UserChannel userChannel2 = UserChannel.builder().user(user).channel(savedChannel2).build();
        UserChannel userChannel3 = UserChannel.builder().user(user).channel(savedChannel3).build();
        userChannelRepository.save(userChannel1);
        userChannelRepository.save(userChannel2);
        userChannelRepository.saveAndFlush(userChannel3);

        // when
        System.out.println("======================== test query start =============================");
        var channels = upperLayerService.getChannelsByUserId(user.getId());
        System.out.println("=======================================================================");

        assert channels.stream().map(ChannelResponseDto::getChannelName)
                .toList()
                .equals(
                        List.of(
                                "channel1",
                                savedChannel1.getChannelName(),
                                savedChannel2.getChannelName(),
                                savedChannel3.getChannelName()
                        )
                );
    }

}