package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        List<CommentResponseDto> responseDtos = new ArrayList<>();
        for (Comment comment : comments) {
            CommentResponseDto responseDto = CommentResponseDto.builder()
                    .id(comment.getId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .userId(comment.getUser().getId())
                    .username(comment.getUser().getNickname()).build();

            Set<CommentEmoji> emojis = comment.getEmojis();
            for (CommentEmoji emoji : emojis) {
                switch (emoji.getEmojiType()) {
                    case Emoji1 -> responseDto.addUserToEmoji1(emoji.getUser().getNickname());
                    case Emoji2 -> responseDto.addUserToEmoji2(emoji.getUser().getNickname());
                    case Emoji3 -> responseDto.addUserToEmoji3(emoji.getUser().getNickname());
                    case Emoji4 -> responseDto.addUserToEmoji4(emoji.getUser().getNickname());
                }
            }

            responseDtos.add(responseDto);
        }

        return responseDtos;
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
    public String updateComment(Long commentId, String content, Long userId) {
        Comment comment = getComment(commentId);
        comment.updateContent(content);
        return "Success";
    }

    @Override
    @Transactional
    public String deleteComment(Long commentId, Long userId) {
        Comment comment = getComment(commentId);
        commentRepository.delete(comment);
        return "Success";
    }

    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            // 수정 예정, 엔터티 찾을 수 없음
            throw new IllegalArgumentException();
        }
        Comment comment = optionalComment.get();
        return comment;
    }
}
