package com.sparta.sbug.emoji.repository.query;

import static com.sparta.sbug.common.exceptions.ErrorCode.COMMENT_EMOJI_ERROR;
import static com.sparta.sbug.emoji.entity.QCommentEmoji.commentEmoji;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.entity.EmojiType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

// lombok
@Slf4j
@RequiredArgsConstructor

// springframework stereotype
@Repository
public class CommentEmojiQueryImpl implements CommentEmojiQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CommentEmoji getCommentEmojiOrNull(Long commentId, Long userId, EmojiType emojiType) {
        try {
            return jpaQueryFactory.selectFrom(commentEmoji)
                    .where(commentEmoji.comment.id.eq(commentId)
                            .and(commentEmoji.user.id.eq(userId))
                            .and(commentEmoji.emojiType.eq(emojiType)))
                    .fetchOne();
        } catch (NonUniqueResultException e) {
            log.error(e.getMessage());
        }

        throw new CustomException(COMMENT_EMOJI_ERROR);
    }

    @Override
    public List<EmojiCountDto> getCommentEmojiCount(List<Long> commentIds) {
        return jpaQueryFactory.select(Projections.constructor(EmojiCountDto.class, commentEmoji.comment.id, commentEmoji.emojiType, commentEmoji.count()))
                .from(commentEmoji)
                .groupBy(commentEmoji.comment_id, commentEmoji.emojiType)
                .having(commentEmoji.comment_id.in(commentIds))
                .fetch();
    }
}
