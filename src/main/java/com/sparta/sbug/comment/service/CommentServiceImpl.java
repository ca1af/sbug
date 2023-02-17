package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllCommentsInThread(Long threadId, PageDto pageDto) {
        Page<Comment> pageComments = commentRepository.findCommentsByThreadId(threadId, pageDto.toPageable());
        List<Comment> comments = pageComments.getContent();
        List<CommentResponseDto> responseDtos = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDto dto = CommentResponseDto.of(comment);
            dto.setEmojis(comment.getEmojis().stream().map(EmojiResponseDto::of).collect(Collectors.toList()));
            responseDtos.add(dto);
        }
        return responseDtos;
    }

    @Override
    @Transactional
    public void createComment(Thread thread, String content, User user) {
        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .build();
        comment.setThread(thread);
        commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void updateComment(Comment comment, String content) {
        comment.updateContent(content);
    }

    @Override
    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new NoSuchElementException("댓글을 찾을 수 없었습니다.");
        }
        return optionalComment.get();
    }
}
