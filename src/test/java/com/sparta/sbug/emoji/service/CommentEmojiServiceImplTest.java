package com.sparta.sbug.emoji.service;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.service.CommentServiceImpl;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.repository.CommentEmojiRepository;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import com.sparta.sbug.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CommentEmojiServiceImplTest {
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    CommentEmojiRepository commentEmojiRepository;
    @Autowired
    UserServiceImpl userService;
    @Test
    void createCommentEmoji() {
        Comment comment = commentService.getComment(1L);
        User user = userService.getUser("user1");
        CommentEmoji commentEmoji = new CommentEmoji("Emoji1", user, comment);
        System.out.println("commentEmoji = " + commentEmoji.getId());

        commentEmojiRepository.save(commentEmoji);

        assertEquals(commentEmoji.getId(), 3L);
    }

    @Test
    void deleteCommentEmoji() {
        commentEmojiRepository.deleteById(2L);

        assertThrows(IllegalArgumentException.class, () -> commentEmojiRepository.findById(2L).orElseThrow(
                () -> new IllegalArgumentException("없음")
        ));
    }
}