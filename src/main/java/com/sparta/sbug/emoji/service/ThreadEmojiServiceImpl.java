package com.sparta.sbug.emoji.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.emoji.entity.QThreadEmoji;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.emoji.repository.ThreadEmojiRepository;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service

// springframework transaction
@Transactional
public class ThreadEmojiServiceImpl implements ThreadEmojiService {

    private final ThreadEmojiRepository threadEmojiRepository;
    private final JPAQueryFactory queryFactory;


    // ThreadEmoji 생성 혹은 삭제
    @Override
    public boolean reactThreadEmoji(String emojiType, User user, Thread thread) {
        ThreadEmoji threadEmoji = threadEmojiRepository.getThreadEmojiOrNull(thread.getId(), user.getId(), EmojiType.valueOf(emojiType));

        if (threadEmoji != null) {
            threadEmojiRepository.delete(threadEmoji);
            return false;
        } else {
            ThreadEmoji threadEmoji2 = new ThreadEmoji(emojiType, user, thread);
            threadEmojiRepository.save(threadEmoji2);
            return true;
        }
    }

    @Override
    public List<EmojiCountDto> getThreadEmojiCount(Long threadId) {
        return threadEmojiRepository.getThreadEmojiCount(threadId);
    }

    @Override
    public List<EmojiCountDto> getThreadsEmojiCount(List<Long> threadIds) {
        return threadEmojiRepository.getThreadsEmojiCount(threadIds);
    }

    @Transactional(readOnly = true)
    @Override
    public void findAll(String emojiType, Long threadId, Long userId) {
        QThreadEmoji qThreadEmoji = QThreadEmoji.threadEmoji;

        ThreadEmoji threadEmoji = queryFactory
                .selectFrom(qThreadEmoji)
                .where(qThreadEmoji.thread.id.eq(threadId)
                        .and(qThreadEmoji.user.id.eq(userId))
                        .and(qThreadEmoji.emojiType.eq(EmojiType.valueOf(emojiType))))
                .fetchOne();
    }
}
