package com.sparta.sbug.chatroom.repository;

import static com.sparta.sbug.chatroom.entity.QChatRoom.chatRoom;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.chatroom.dto.ChatRoomDto;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

// lombok
@RequiredArgsConstructor
public class ChatRoomRepositoryQueryImpl implements ChatRoomRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Long findChatRoomIdByUsers(Long userId1, Long userId2) {
        Optional<ChatRoom> optionalChatRoom = Optional.ofNullable(
                jpaQueryFactory.select(chatRoom)
                        .from(chatRoom)
                        .where(
                                (chatRoom.user1.id.eq(userId1).and(chatRoom.user2.id.eq(userId2))).or(
                                        chatRoom.user1.id.eq(userId2).and(chatRoom.user2.id.eq(userId1))
                                )
                        ).fetchOne()
        );

        if (optionalChatRoom.isPresent()) {
            return optionalChatRoom.get().getId();
        }

        return -1L;
    }

    @Override
    public List<ChatRoomDto> getChatRoomListByUserId(Long userId) {
        return jpaQueryFactory.select(Projections.constructor(ChatRoomDto.class,
                        chatRoom.id,
                        new CaseBuilder().when(chatRoom.user1.id.eq(userId)).then(chatRoom.user2.email).otherwise(chatRoom.user1.email),
                        new CaseBuilder().when(chatRoom.user1.id.eq(userId)).then(chatRoom.user2.nickname).otherwise(chatRoom.user1.nickname),
                        new CaseBuilder().when(chatRoom.user1.id.eq(userId)).then(chatRoom.user2.profileImage).otherwise(chatRoom.user1.profileImage)
                ))
                .from(chatRoom)
                .where(chatRoom.user1.id.eq(userId).or(chatRoom.user2.id.eq(userId)))
                .fetch();
    }
}
