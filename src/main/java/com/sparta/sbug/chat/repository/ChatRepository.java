package com.sparta.sbug.chat.repository;

import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.repository.query.ChatRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

// springframework data repository
@RepositoryDefinition(domainClass = Chat.class, idClass = Long.class)
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryQuery {

}
