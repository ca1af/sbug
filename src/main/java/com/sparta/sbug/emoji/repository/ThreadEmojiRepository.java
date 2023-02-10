package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThreadEmojiRepository extends JpaRepository<ThreadEmoji, Long> {
    Optional<ThreadEmoji> findByIdAndUser(Long id, User user);
}
