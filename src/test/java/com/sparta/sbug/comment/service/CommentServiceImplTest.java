package com.sparta.sbug.comment.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.entity.UserRole;
import com.sparta.sbug.user.repository.UserRepository;
import com.sparta.sbug.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    @Transactional
    public void createUser() {
        User user1 = User.builder().email("user1").password("password1").nickname("뽀로로").build();
        user1.setUserRole(UserRole.USER);
        User savedUser1 = userRepository.save(user1);

        User user2 = User.builder().email("user2").password("password2").nickname("루피").build();
        user2.setUserRole(UserRole.USER);
        User savedUser2 = userRepository.save(user2);

        Channel channel = Channel.builder()
                .user(savedUser1)
                .adminEmail(savedUser1.getEmail())
                .channelName("channel").build();

        Channel savedChannel = channelRepository.save(channel);
        savedUser1.addChannel(savedChannel);
        savedUser2.addChannel(savedChannel);

        this.channel = savedChannel;
        this.user1 = savedUser1;
        this.user2 = savedUser2;

        Thread thread = new Thread(this.channel, this.user1, "안녕하세요");
        Thread savedThread = threadRepository.save(thread);

        this.channel.addThread(savedThread);
        this.thread = savedThread;
    }

    @Test
    @DisplayName("Comment: Get All Comments In Thread")
    @Transactional
    public void getAllCommentsInThread() {
        // give
        Comment comment1 = Comment.builder()
                .user(user1).thread(thread).content("comment1").build();
        Comment comment2 = Comment.builder()
                .user(user2).thread(thread).content("comment2").build();
        Comment savedComment1 = commentRepository.save(comment1);
        Comment savedComment2 = commentRepository.save(comment2);

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
        // give

        // when
        String result1 = commentService.createComment(this.thread, "comment1", user1);
        String result2 = commentService.createComment(this.thread, "comment2", user2);

        // then
        assert result1.equals("Success");
        assert result2.equals("Success");
        var comments = commentRepository.findCommentsByThreadId(this.thread.getId(), pageDto.toPageable());
        assert comments.getContent().stream().map(Comment::getContent).collect(Collectors.toSet())
                .equals(Set.of("comment1", "comment2"));
    }

    @Test
    @DisplayName("Comment: update comment")
    @Transactional
    public void updateComment() {
        // give
        Comment comment1 = Comment.builder()
                .user(user1).thread(thread).content("comment1").build();
        Comment savedComment1 = commentRepository.save(comment1);


        // when
        System.out.println("======================= test query start! =======================");
        String result = commentService.updateComment(savedComment1.getId(), "updated comment", user1.getId());

        // then
        assert result.equals("Success");
        assert savedComment1.getContent().equals("updated comment");
    }

    @Test
    @DisplayName("Comment: delete comment")
    @Transactional
    public void deleteComment() {
        // give
        Comment comment1 = Comment.builder()
                .user(user1).thread(thread).content("comment1").build();
        Comment savedComment1 = commentRepository.save(comment1);


        // when
        System.out.println("======================= test query start! =======================");
        String result = commentService.deleteComment(savedComment1.getId(), user1.getId());

        // then
        assert result.equals("Success");
        assert commentRepository.findById(savedComment1.getId()).isEmpty();

    }
}