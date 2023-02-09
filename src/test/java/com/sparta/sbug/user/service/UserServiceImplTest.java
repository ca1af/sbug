package com.sparta.sbug.user.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.entity.QChannel;
import com.sparta.sbug.user.entity.QUser;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import com.sparta.sbug.userchannel.enttiy.QUserChannel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserServiceImplTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    static User userSample = User.builder().email("email").password("password").nickname("nickname").build();

    @Test
    void signup() {
        Optional<User> saveUser = Optional.of(userRepository.save(userSample));
        Optional<User> foundUser = userRepository.findByEmail("email");
        assertEquals(saveUser,foundUser);
    }

    @Test
    void login() {
    }

    @Test
    void unregister() {
    }

    @Test
    void myPage() {
    }

    @Test
    void update() {
    }

    @Test
    void testGetMyChannels() {
        User user = User.builder().email("email").password("password").nickname("nickname").build();
        userRepository.save(user);

        var qChannel = QChannel.channel;
        var qUser = QUser.user;
        var qUserChannel = QUserChannel.userChannel;

        List<Channel> fetch = queryFactory
                .select(qChannel)
                .from(qUserChannel)
                .where(qUserChannel.user.id.eq(user.getId()))
                .fetch();
        // contain 은 같은 값이 많을 때만 시도해보자. <<
    }
}