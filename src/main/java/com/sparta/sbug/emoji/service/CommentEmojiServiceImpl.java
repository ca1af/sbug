package com.sparta.sbug.emoji.service;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.service.CommentServiceImpl;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.emoji.repository.CommentEmojiRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentEmojiServiceImpl implements CommentEmojiService {

    private final CommentEmojiRepository commentEmojiRepository;
    private final CommentServiceImpl commentService;


    // CommentEmoji 생성
    @Override
    public void createCommentEmoji(String emojiType, User user, Long commentId){
        Comment comment = commentService.getComment(commentId);
        Optional<CommentEmoji> optionalEmoji = commentEmojiRepository.findByEmojiTypeAndCommentAndUser(EmojiType.valueOf(emojiType), comment, user);
        if (optionalEmoji.isPresent()) {
            throw new IllegalArgumentException("이미 동일한 이모지 반응이 존재합니다.");
        }
        CommentEmoji commentEmoji = new CommentEmoji(emojiType, user, comment);
        commentEmojiRepository.save(commentEmoji);
    }

    // CommentEmoji 삭제
    @Override
    public void deleteCommentEmoji(String emojiType, User user, Long commentId) {
        Comment comment = commentService.getComment(commentId);
        Optional<CommentEmoji> optionalEmoji = commentEmojiRepository.findByEmojiTypeAndCommentAndUser(EmojiType.valueOf(emojiType), comment, user);
        if (optionalEmoji.isEmpty()) {
            throw new NoSuchElementException("해당 이모지 반응을 찾을 수 없습니다.");
        }
        commentEmojiRepository.delete(optionalEmoji.get());
    }
}
