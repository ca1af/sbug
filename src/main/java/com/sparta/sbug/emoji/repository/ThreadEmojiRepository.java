package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.emoji.entity.ThreadEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadEmojiRepository extends JpaRepository<ThreadEmoji, Long> {

}
