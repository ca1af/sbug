package com.sparta.sbug.comment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback(value = false)
class CommentServiceImplTest {

    @Test
    @DisplayName("Comment: Get All Comments In Thread")
    @Transactional
    public void getAllCommentsInThread() {

    }

    @Test
    @DisplayName("Comment: Create comment")
    @Transactional
    public void createComment() {

    }

    @Test
    @DisplayName("Comment: update comment")
    @Transactional
    public void updateComment() {

    }

    @Test
    @DisplayName("Comment: delete comment")
    @Transactional
    public void deleteComment() {

    }
}
