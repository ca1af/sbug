package com.sparta.sbug.user.service;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserServiceImplTest {
    @Autowired
    UserRepository userRepository;
    @Test
    void signup() {
        User user = User.builder().email("email").password("password").nickname("nickname").build();
        Optional<User> saveUser = Optional.of(userRepository.save(user));
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
}