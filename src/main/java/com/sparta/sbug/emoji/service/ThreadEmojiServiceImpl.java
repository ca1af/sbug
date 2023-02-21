package com.sparta.sbug.emoji.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.emoji.entity.QThreadEmoji;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.emoji.repository.ThreadEmojiRepository;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.service.ThreadServiceImpl;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service

// springframework transaction
@Transactional
public class ThreadEmojiServiceImpl implements ThreadEmojiService {

    private final ThreadEmojiRepository threadEmojiRepository;
    private final ThreadServiceImpl threadService;
    private final JPAQueryFactory queryFactory;


    // ThreadEmoji 생성 혹은 삭제
    @Override
    public boolean reactThreadEmoji(String emojiType, User user, Long threadId) {
        QThreadEmoji qThreadEmoji = QThreadEmoji.threadEmoji;
        ThreadEmoji threadEmoji = queryFactory
                .selectFrom(qThreadEmoji)
                .where(qThreadEmoji.thread.id.eq(threadId)
                        .and(qThreadEmoji.user.id.eq(user.getId()))
                        .and(qThreadEmoji.emojiType.eq(EmojiType.valueOf(emojiType))))
                .fetchOne();

        if (threadEmoji!=null) {
            threadEmojiRepository.delete(threadEmoji);
            return false;
        } else {
            Thread thread = threadService.findThreadById(threadId);
            ThreadEmoji threadEmoji2 = new ThreadEmoji(emojiType, user, thread);
            threadEmojiRepository.save(threadEmoji2);
            return true;
        }
    }

    // ThreadEmoji 삭제
    @Override
    public void deleteThreadEmoji(String emojiType, User user, Long threadId) {
        QThreadEmoji qThreadEmoji = QThreadEmoji.threadEmoji;

        ThreadEmoji threadEmoji = queryFactory
                .selectFrom(qThreadEmoji)
                .where(qThreadEmoji.thread.id.eq(threadId)
                        .and(qThreadEmoji.user.id.eq(user.getId()))
                        .and(qThreadEmoji.emojiType.eq(EmojiType.valueOf(emojiType))))
                .fetchOne();
        if (threadEmoji==null){
            throw new NoSuchElementException("이모지 오류가 발생했습니다. 다시 시도 해 주세요");
        }

        threadEmojiRepository.delete(threadEmoji);
    }
    @Transactional(readOnly = true)
    @Override
    public void findAll(String emojiType, Long threadId, Long userId){
        QThreadEmoji qThreadEmoji = QThreadEmoji.threadEmoji;

        ThreadEmoji threadEmoji = queryFactory
                .selectFrom(qThreadEmoji)
                .where(qThreadEmoji.thread.id.eq(threadId)
                        .and(qThreadEmoji.user.id.eq(userId))
                        .and(qThreadEmoji.emojiType.eq(EmojiType.valueOf(emojiType))))
                .fetchOne();
    }
}
