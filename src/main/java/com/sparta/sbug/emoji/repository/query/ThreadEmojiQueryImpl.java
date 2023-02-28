package com.sparta.sbug.emoji.repository.query;

import static com.sparta.sbug.common.exceptions.ErrorCode.THREAD_EMOJI_ERROR;
import static com.sparta.sbug.emoji.entity.QThreadEmoji.threadEmoji;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

// lombok
@Slf4j
@RequiredArgsConstructor

// springframework stereotype
@Repository
public class ThreadEmojiQueryImpl implements ThreadEmojiQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ThreadEmoji getThreadEmojiOrNull(Long threadId, Long userId, EmojiType emojiType) {
        try {
            return jpaQueryFactory.selectFrom(threadEmoji)
                    .where(
                            threadEmoji.thread_id.eq(threadId)
                                    .and(threadEmoji.user.id.eq(userId))
                                    .and(threadEmoji.emojiType.eq(emojiType))
                    )
                    .fetchOne();
        } catch (NonUniqueResultException e) {
            log.error(e.getMessage());
        }

        throw new CustomException(THREAD_EMOJI_ERROR);
    }

    @Override
    public List<EmojiCountDto> getThreadEmojiCount(Long threadId) {
        return jpaQueryFactory.select(Projections.constructor(EmojiCountDto.class, threadEmoji.thread.id, threadEmoji.emojiType, threadEmoji.count()))
                .from(threadEmoji)
                .where(threadEmoji.thread_id.eq(threadId))
                .fetch();
    }

    @Override
    public List<EmojiCountDto> getThreadsEmojiCount(List<Long> threadIds) {
        return jpaQueryFactory.select(Projections.constructor(EmojiCountDto.class, threadEmoji.thread.id, threadEmoji.emojiType, threadEmoji.count()))
                .from(threadEmoji)
                .where(threadEmoji.thread_id.in(threadIds))
                .fetch();
    }
}
