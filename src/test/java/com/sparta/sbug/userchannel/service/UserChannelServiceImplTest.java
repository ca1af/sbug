package com.sparta.sbug.userchannel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
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
        // give
        User user = userRepository.findByEmailAndInUseIsTrue("user1").orElseThrow();
        Channel channel1 = channelRepository.findById(1L).orElseThrow();
        Channel channel2 = channelRepository.findById(2L).orElseThrow();
        UserChannel userChannel1 = UserChannel.builder().user(user).channel(channel1).build();
        UserChannel userChannel2 = UserChannel.builder().user(user).channel(channel2).build();
        var savedUserChannel1 = userChannelRepository.save(userChannel1);
        var savedUserChannel2 = userChannelRepository.saveAndFlush(userChannel2);

        // when
        System.out.println("======================== test query start =============================");
        var userChannels = userChannelService.getUserChannelsByUserId(user.getId());
        System.out.println("=======================================================================");

        // then
        assert userChannels.equals(List.of(savedUserChannel1, savedUserChannel2));
    }

    @Test
    @DisplayName("UserChannel Service : Create User-Channel ")
    @Transactional
    void createUserChannel() {
        // give
        User user = userRepository.findByEmailAndInUseIsTrue("user1").orElseThrow();
        Channel channel = Channel.builder()

                .channelName("test channel").build();

        Channel savedChannel = channelRepository.saveAndFlush(channel);

        // when
        System.out.println("======================== test query start =============================");
        Long id = userChannelService.createUserChannel(user, savedChannel);
        userChannelRepository.flush();
        System.out.println("=======================================================================");

        // then
        UserChannel createdUserChannel = userChannelRepository.findById(id).orElseThrow();
        assert createdUserChannel.getUser().equals(user);
    }

    @Test
    @DisplayName("UserChannel Service : Delete User-Channels About Channel")
    @Transactional
    void deleteUserChannelsAboutChannel() {
        // give
        User user = userRepository.findByEmailAndInUseIsTrue("user1").orElseThrow();
        Channel channel1 = channelRepository.findById(1L).orElseThrow();
        Channel channel2 = channelRepository.findById(2L).orElseThrow();
        UserChannel userChannel1 = UserChannel.builder().user(user).channel(channel1).build();
        UserChannel userChannel2 = UserChannel.builder().user(user).channel(channel2).build();
        userChannelRepository.save(userChannel1);
        userChannelRepository.saveAndFlush(userChannel2);

        // when
        System.out.println("======================== test query start =============================");
        userChannelService.deleteUserChannelsAboutChannel(channel1.getId());
        System.out.println("=======================================================================");

        // then
        List<UserChannel> userChannels = userChannelRepository.findAll();
        for (UserChannel userChannel : userChannels) {
            assert !userChannel.getChannel().getId().equals(channel1.getId());
        }
    }

    @Test
    @DisplayName("UserChannel Service : Delete User-Channels About Channel")
    @Transactional
    void deleteUserChannelsAboutUser() {
        // give
        User user1 = userRepository.findByEmailAndInUseIsTrue("user1").orElseThrow();
        User user2 = userRepository.findByEmailAndInUseIsTrue("user2").orElseThrow();
        Channel channel = channelRepository.findById(1L).orElseThrow();
        UserChannel userChannel1 = UserChannel.builder().user(user1).channel(channel).build();
        UserChannel userChannel2 = UserChannel.builder().user(user2).channel(channel).build();
        userChannelRepository.save(userChannel1);
        userChannelRepository.saveAndFlush(userChannel2);

        // when
        System.out.println("======================== test query start =============================");
        userChannelService.deleteUserChannelsAboutUser(user1.getId());
        System.out.println("=======================================================================");

        // then
        List<UserChannel> userChannels = userChannelRepository.findAll();
        for (UserChannel userChannel : userChannels) {
            assert !userChannel.getUser().getId().equals(user1.getId());
        }
    }

    @Test
    @DisplayName("UserChannel Service : Delete User-Channels By User And Channel")
    @Transactional
    void deleteUserChannelByUserAndChannel() {
        // give
        User user1 = userRepository.findByEmailAndInUseIsTrue("user1").orElseThrow();
        User user2 = userRepository.findByEmailAndInUseIsTrue("user2").orElseThrow();
        Channel channel1 = channelRepository.findById(1L).orElseThrow();
        Channel channel2 = channelRepository.findById(2L).orElseThrow();
        UserChannel userChannel1 = UserChannel.builder().user(user1).channel(channel1).build();
        UserChannel userChannel2 = UserChannel.builder().user(user1).channel(channel2).build();
        UserChannel userChannel3 = UserChannel.builder().user(user2).channel(channel1).build();
        userChannelRepository.save(userChannel1);
        var savedUserChannel2 = userChannelRepository.saveAndFlush(userChannel2);
        userChannelRepository.saveAndFlush(userChannel3);


        // when
        System.out.println("======================== test query start =============================");
        userChannelService.deleteUserChannelByUserAndChannel(user1, channel1);
        System.out.println("=======================================================================");

        // then
        List<UserChannel> thenUserChannels = userChannelRepository.findAll();
        for (UserChannel thenUserChannel : thenUserChannels) {
            assert !(thenUserChannel.getUser().equals(user1) && thenUserChannel.getChannel().equals(channel1));
        }

        assert userChannelRepository.findById(savedUserChannel2.getId()).isPresent();
        assert userChannelRepository.findById(savedUserChannel2.getId()).isPresent();
    }

}