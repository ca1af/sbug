package com.sparta.sbug.chat.repository;

import com.sparta.sbug.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Chat.class, idClass = Long.class)
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
