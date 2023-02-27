package com.sparta.sbug.emoji.repository;

import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.emoji.repository.query.CommentEmojiQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

// springframework data repository
@RepositoryDefinition(domainClass = CommentEmoji.class, idClass = Long.class)
public interface CommentEmojiRepository extends JpaRepository<CommentEmoji, Long>, CommentEmojiQuery {

}
