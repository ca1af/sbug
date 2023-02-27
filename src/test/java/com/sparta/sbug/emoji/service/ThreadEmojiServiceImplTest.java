package com.sparta.sbug.emoji.service;

import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.emoji.repository.ThreadEmojiRepository;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.service.ThreadServiceImpl;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ThreadEmojiServiceImplTest {
    @Autowired
    ThreadServiceImpl threadService;
    @Autowired
    ThreadEmojiRepository threadEmojiRepository;
    @Autowired
    UserServiceImpl userService;

    @Test
    void createThreadEmoji() {
        Thread thread = threadService.findThreadById(1L);
        User user1 = userService.getUserByEmail("user1");
        ThreadEmoji threadEmoji = new ThreadEmoji("Emoji1",user1,thread);
        threadEmojiRepository.save(threadEmoji);

        ThreadEmoji threadEmoji1 = threadEmojiRepository.findById(2L).orElseThrow(
                () -> new IllegalArgumentException("없음")
        );

        assertEquals(threadEmoji1, threadEmoji1);
    }


    @Test
    void deleteThreadEmoji() {
        ThreadEmoji threadEmoji = threadEmojiRepository.findById(1L).orElseThrow(
                () -> new IllegalArgumentException("없음")
        );

        threadEmojiRepository.delete(threadEmoji);


        assertThrows(IllegalArgumentException.class, () -> threadEmojiRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("없음")));
    }
}