package com.sparta.sbug.emoji.service;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.entity.*;
import com.sparta.sbug.emoji.repository.CommentEmojiRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentEmojiServiceImpl implements CommentEmojiService {

    private final CommentEmojiRepository commentEmojiRepository;

    @Override
    public boolean reactCommentEmoji(String emojiType, User user, Comment comment) {
        CommentEmoji commentEmoji = commentEmojiRepository.getCommentEmojiOrNull(comment.getId(), user.getId(), EmojiType.valueOf(emojiType));

        if (commentEmoji != null) {
            commentEmojiRepository.delete(commentEmoji);
            return false;
        } else {
            CommentEmoji newEmojiReact = new CommentEmoji(emojiType, user, comment);
            commentEmojiRepository.save(newEmojiReact);
            return true;
        }
    }

    @Override
    @Transactional
    public List<EmojiCountDto> getCommentEmojiCount(List<Long> commentIds) {
        return commentEmojiRepository.getCommentEmojiCount(commentIds);
    }
}
