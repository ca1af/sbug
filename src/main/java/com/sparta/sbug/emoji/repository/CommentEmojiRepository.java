package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.emoji.entity.CommentEmoji;
import org.springframework.data.jpa.repository.JpaRepository;

// springframework data repository
@RepositoryDefinition(domainClass = CommentEmoji.class, idClass = Long.class)
public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long>, CommentEmojiQuery {

}
