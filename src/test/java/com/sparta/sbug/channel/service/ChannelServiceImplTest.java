package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ChannelServiceImplTest {

    // Repository
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;
    // Service
    @Autowired
    ChannelService channelService;

    @Test
    @DisplayName("Channel Service : Get Channel By Channel ID")
    void getChannelById() {
        // give
        User user = userRepository.findByEmail("user1").orElseThrow();

        Channel newChannel = Channel.builder()
                .adminEmail(user.getEmail())
                .channelName("test channel").build();

        Channel savedChannel = channelRepository.saveAndFlush(newChannel);

        // when
        System.out.println("======================== test query start =============================");
        Channel channel = channelService.getChannelById(savedChannel.getId());
        System.out.println("=======================================================================");

        // then
        assert channel.getChannelName().equals(savedChannel.getChannelName());
        assert channel.getId().equals(savedChannel.getId());

    }

    @Test
    @DisplayName("Channel Service : Create Channel")
    void createChannel() {
        // give
        User user = userRepository.findByEmail("user1").orElseThrow();
        String channelName = "new test channel";

        // when
        System.out.println("======================== test query start =============================");
        Channel newChannel = channelService.createChannel(user, channelName);
        System.out.println("=======================================================================");

        // then
        assert newChannel.getChannelName().equals(channelName);
        assert newChannel.getAdminEmail().equals(user.getEmail());

    }

    @Test
    @DisplayName("Channel Service : Update Channel Name")
    void updateChannelName() {
        // give
        User user1 = userRepository.findByEmail("user1").orElseThrow();
        Channel channel = channelRepository.findById(1L).orElseThrow();
        String newChannelName = "New Channel Name";

        // when
        System.out.println("======================== test query start =============================");
        channelService.updateChannelName(channel.getId(), user1, newChannelName);
        System.out.println("=======================================================================");

        // then
        Channel updatedChannel = channelRepository.findById(1L).orElseThrow();
        assert updatedChannel.getChannelName().equals(newChannelName);
    }

    @Test
    @DisplayName("Channel Service : Delete Channel")
    // 채널을 삭제하는 메서드 테스트입니다, Thread 등과 매핑되지 않은 빈 채널로 테스트 해야 합니다.
    void deleteChannel() {
        // give
        User user1 = userRepository.findByEmail("user1").orElseThrow();
        Channel newChannel = Channel.builder()
                .adminEmail(user1.getEmail())
                .channelName("test channel").build();

        Channel savedChannel = channelRepository.saveAndFlush(newChannel);
        var id = savedChannel.getId();

        // when
        System.out.println("======================== test query start =============================");
        channelService.deleteChannel(savedChannel.getId(), user1);
        System.out.println("=======================================================================");

        // then
        Optional<Channel> deletedChannel = channelRepository.findById(id);
        assert deletedChannel.isEmpty();

    }
}