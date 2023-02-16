package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThreadEmojiRepository extends JpaRepository<ThreadEmoji, Long> {

    /**
     * 쓰레드의 ID와 유저 ID로 쓰레드에 남겨진 이모지 반응을 조회
     *
     * @param id   대상 쓰레드 ID
     * @param user 유저 ID
     * @return Optional&lt;ThreadEmoji&gt;
     */
    Optional<ThreadEmoji> findByIdAndUser(Long id, User user);
    Optional<ThreadEmoji> findByEmojiTypeAndThreadAndUser(EmojiType emojiType, Thread thread, User user);
}
