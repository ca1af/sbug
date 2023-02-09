package com.sparta.sbug.emoji.service;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.service.CommentServiceImpl;
import com.sparta.sbug.emoji.dto.EmojiRequestDto;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.emoji.repository.CommentEmojiRepository;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentEmojiServiceImpl implements CommentEmojiService {

    private final CommentEmojiRepository commentEmojiRepository;
    private final CommentServiceImpl commentService;


    // CommentEmoji 생성
    @Override
    public String createCommentEmoji(Long commentId, String emojiType, User user){
        Comment comment = commentService.getComment(commentId);
        CommentEmoji commentEmoji = new CommentEmoji(emojiType, user, comment);
        commentEmojiRepository.save(commentEmoji);
        return "channelName/thread/{id}";
    }

    // CommentEmoji 삭제
    @Override
    public String deleteCommentEmoji(Long emojiId, User user) {
        CommentEmoji commentEmoji = commentEmojiRepository.findByIdAndUser(emojiId, user).orElseThrow();
        commentEmojiRepository.delete(commentEmoji);
        return "channelName/thread/{id}";
    }

}
