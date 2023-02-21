package com.sparta.sbug.emoji.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.service.CommentServiceImpl;
import com.sparta.sbug.emoji.entity.*;
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
    private final JPAQueryFactory queryFactory;


    // CommentEmoji 생성
    @Override
    public boolean reactCommentEmoji(String emojiType, User user, Long commentId){
        QCommentEmoji qCommentEmoji = QCommentEmoji.commentEmoji;

        CommentEmoji commentEmoji = queryFactory
                .selectFrom(qCommentEmoji)
                .where(qCommentEmoji.comment.id.eq(commentId)
                        .and(qCommentEmoji.user.id.eq(user.getId()))
                        .and(qCommentEmoji.emojiType.eq(EmojiType.valueOf(emojiType))))
                .fetchOne();
        if (commentEmoji!= null) {
            commentEmojiRepository.delete(commentEmoji);
            return false;
        } else {
            Comment comment = commentService.getComment(commentId);
            CommentEmoji commentEmoji2 = new CommentEmoji(emojiType, user, comment);
            commentEmojiRepository.save(commentEmoji2);
            return true;
        }
    }

    // CommentEmoji 삭제
    @Override
    public void deleteCommentEmoji(String emojiType, User user, Long commentId) {
        QCommentEmoji qCommentEmoji = QCommentEmoji.commentEmoji;

        CommentEmoji commentEmoji = queryFactory
                .selectFrom(qCommentEmoji)
                .where(qCommentEmoji.comment.id.eq(commentId)
                        .and(qCommentEmoji.user.id.eq(user.getId()))
                        .and(qCommentEmoji.emojiType.eq(EmojiType.valueOf(emojiType))))
                .fetchOne();

        if (commentEmoji==null) {
            throw new NoSuchElementException("해당 이모지 반응을 찾을 수 없습니다.");
        }
        commentEmojiRepository.delete(commentEmoji);
    }
}
