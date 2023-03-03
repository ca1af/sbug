package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.common.paging.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import com.sparta.sbug.emoji.service.CommentEmojiService;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.cache.CacheNames;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.sparta.sbug.common.exceptions.ErrorCode.COMMENT_NOT_FOUND;
import static com.sparta.sbug.common.exceptions.ErrorCode.USER_COMMENT_FORBIDDEN;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentEmojiService commentEmojiService;


   // CRUD
    
    @Override
    public CommentResponseDto createComment(Thread thread, String content, User user) {
        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .build();
        comment.setThread(thread);
        return CommentResponseDto.of(commentRepository.save(comment), null);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<CommentResponseDto> getAllCommentsInThread(Long threadId, PageDto pageDto) {
        Slice<Comment> comments = commentRepository.findCommentsByThreadIdAndInUseIsTrue(threadId, pageDto.toPageable());
        List<Long> commentIds = comments.getContent().stream().map(Comment::getId).toList();
        List<EmojiCountDto> emojiCountDtoList = commentEmojiService.getCommentEmojiCount(commentIds);
        Map<Long, List<EmojiResponseDto>> commentEmojiCountMap = EmojiResponseDto.getEmojiCountMap(emojiCountDtoList);
        return comments.map(comment -> CommentResponseDto.of(comment, commentEmojiCountMap));
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            throw new CustomException(COMMENT_NOT_FOUND);
        }
        return optionalComment.get();
    }

    @Override
    @Transactional
    public void updateComment(Long commentId, String content, User user) {
        Comment comment = validateUserAuth(commentId, user);
        comment.updateContent(content);
    }

    @Override
    public void disableComment(Long commentId, User user) {
        Comment comment = validateUserAuth(commentId, user);
        commentRepository.disableCommentByCommentId(comment.getId());
    }


    // 유저 권한 검증

    @Override
    public Comment validateUserAuth(Long commentId, User user) {
        Comment comment = getComment(commentId);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(USER_COMMENT_FORBIDDEN);
        }

        return comment;
    }

    @Override
    @Scheduled(cron = "0 0 5 1 3,6,9,12 *")
    @Transactional
    public void deleteCommentsOnSchedule() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(6);
        commentRepository.deleteComments(localDateTime);
    }

    // Disable //
    @Override
    public void disableCommentByChannelId(Long channelId) {
        commentRepository.disableCommentByChannelId(channelId);
    }

    @Override
    public void disableCommentByThreadId(Long threadId) {
        commentRepository.disableCommentByThreadId(threadId);
    }

    @Override
    @Transactional
    public void disableCommentByAdmin(Long commentId) {
        commentRepository.disableCommentByCommentId(commentId);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.COMMENT, key = "#commentId")
    public boolean reactCommentEmoji(String emojiType, User user, Long commentId) {
        Comment comment = getComment(commentId);
        return commentEmojiService.reactCommentEmoji(emojiType, user, comment);
    }
}
