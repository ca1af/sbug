package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.emoji.entity.EmojiType;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
//    @Query(value = "select * from Emoji e where emoji_type =: emojiType and user_id =:userId and thread_id =:threadId", nativeQuery = true)
    @Query("select ThreadEmoji from ThreadEmoji e where e.emojiType =:emojiType and e.user.id =:userId and e.thread.id =:threadId")
    Optional<ThreadEmoji> findAllByUserIdAndThreadId(@Param("emojiType") EmojiType emojiType, @Param("threadId") Long threadId,@Param("userId") Long userId);
    Optional<ThreadEmoji> findByEmojiTypeAndThreadAndUser(EmojiType emojiType, Thread thread, User user);
}
