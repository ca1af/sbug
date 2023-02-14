package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllCommentsInThread(Long threadId, PageDto pageDto) {
        Page<Comment> pageComments = commentRepository.findCommentsByThreadId(threadId, pageDto.toPageable());
        List<Comment> comments = pageComments.getContent();
        return comments.stream().map(CommentResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String createComment(Thread thread, String content, User user) {
        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .thread(thread).build();
        commentRepository.save(comment);
        return "Success";
    }

    @Override
    @Transactional
    public String updateComment(Comment comment, String content) {
        comment.updateContent(content);
        return "Success";
    }

    @Override
    @Transactional
    public String deleteComment(Comment comment) {
        commentRepository.delete(comment);
        return "Success";
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
