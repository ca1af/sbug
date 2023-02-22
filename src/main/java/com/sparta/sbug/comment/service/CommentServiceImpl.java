package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.COMMENTSINTHREAD, key = "#threadId")
    public Slice<CommentResponseDto> getAllCommentsInThread(Long threadId, PageDto pageDto) {
        Slice<Comment> comments = commentRepository.findCommentsByThreadIdAndInUseIsTrue(threadId, pageDto.toPageable());
        return comments.map(CommentResponseDto::of);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.COMMENTSINTHREAD, key = "#thread.threadId")
    public CommentResponseDto createComment(Thread thread, String content, User user) {
        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .build();
        comment.setThread(thread);
        return CommentResponseDto.of(commentRepository.save(comment));
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.COMMENT, key = "#comment.commentId")
    public void updateComment(Comment comment, String content) {
        comment.updateContent(content);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.COMMENT, key = "#comment.commentId")
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.COMMENT, key = "#commentId")
    public Comment getComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new NoSuchElementException("댓글을 찾을 수 없었습니다.");
        }
        return optionalComment.get();
    }
    @Transactional
    @Override
    public void autoDelete(){
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(6);
        commentRepository.deleteComments(localDateTime);
    }

    @Override
    public boolean existCommentById(Long commentId) {
        return false;
    }
}
