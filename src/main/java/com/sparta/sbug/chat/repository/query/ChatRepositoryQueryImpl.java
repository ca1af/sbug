package com.sparta.sbug.chat.repository.query;

import static com.sparta.sbug.chat.entity.QChat.chat;
import static com.sparta.sbug.user.entity.QUser.user;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.dto.NewMessageCountDto;
import com.sparta.sbug.chat.entity.ChatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// lombok
@RequiredArgsConstructor
public class ChatRepositoryQueryImpl implements ChatRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ChatResponseDto> findMessagesInChatRoom(Long roomId, Pageable pageable) {
        List<ChatResponseDto> chats = jpaQueryFactory.select(
                        Projections.constructor(ChatResponseDto.class, chat.id, chat.room.id,
                                JPAExpressions.select(user.nickname).from(user).where(user.id.eq(chat.sender.id)),
                                JPAExpressions.select(user.nickname).from(user).where(user.id.eq(chat.receiver.id)),
                                chat.receiver.id,
                                chat.message,
                                chat.status,
                                chat.createdAt
                        )
                )
                .from(chat)
                .where(chat.room.id.eq(roomId))
                .orderBy(new OrderSpecifier<>(Order.DESC, chat.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (chats.size() > pageable.getPageSize()) {
            chats.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(chats, pageable, hasNext);
    }

    @Override
    public void convertMessagesStatusToRead(List<Long> chatIds) {
        jpaQueryFactory.update(chat)
                .set(chat.status, ChatStatus.READ)
                .where(chat.id.in(chatIds))
                .execute();
    }

    @Override
    public Map<Long, Long> countNewMessageByChatRoom(Long userId) {
        List<NewMessageCountDto> countDtoList = jpaQueryFactory
                .select(Projections.constructor(NewMessageCountDto.class, chat.room.id, chat.count()))
                .from(chat)
                .groupBy(chat.room.id, chat.receiver.id, chat.status)
                .having(
                        chat.receiver.id.eq(userId).and(chat.status.eq(ChatStatus.NEW))
                ).fetch();

        return countDtoList.stream()
                .collect(Collectors.toMap(NewMessageCountDto::getChatRoomId, NewMessageCountDto::getNewMessageCount));
    }

    @Override
    public Long countNewMessageByReceiverId(Long id) {
        JPAQuery<Long> query = jpaQueryFactory.select(chat.count())
                .from(chat)
                .where(
                        chat.receiver.id.eq(id).and(chat.status.eq(ChatStatus.NEW))
                );
        return query.fetch().get(0);
    }
}
