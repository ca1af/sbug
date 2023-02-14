package com.sparta.sbug.comment.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@Rollback(value = false)
class CommentServiceImplTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentService commentService;
    @Autowired
    ThreadRepository threadRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChannelRepository channelRepository;

    /**
     * 클래스 변수
     */
    private User user1;
    private User user2;
    private Channel channel;
    private Thread thread;
    private PageDto pageDto = PageDto.builder().currentPage(1).size(5).sortBy("createdAt").build();


    @Test
    @DisplayName("Comment: Get All Comments In Thread")
    @Transactional
    public void getAllCommentsInThread() {
        // give
        Comment comment1 = Comment.builder()
                .user(user1).thread(thread).content("comment1").build();
        Comment comment2 = Comment.builder()
                .user(user2).thread(thread).content("comment2").build();

        // when
        List<CommentResponseDto> responseDtoList = commentService.getAllCommentsInThread(this.thread.getId(), pageDto);

        // then
        assert responseDtoList.stream()
                .map(CommentResponseDto::getContent).collect(Collectors.toSet())
                .equals(Set.of("comment1", "comment2"));
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