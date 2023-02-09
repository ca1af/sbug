package com.sparta.sbug.chat.repository;

import static com.sparta.sbug.chat.entity.QChat.chat;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.chat.entity.Chat;
import com.sparta.sbug.chat.entity.ChatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;


@RequiredArgsConstructor
public class ChatRepositoryQueryImpl implements ChatRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Chat> findMessagesInChatRoom(Long roomId, Pageable pageable) {
        JPAQuery<Chat> query = buildQueryForSelectExchangedMessages(chat, roomId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = buildQueryForSelectExchangedMessages(Wildcard.count, roomId);

        List<Chat> chats = query.fetch();
        long totalSize = countQuery.fetch().get(0);

        return PageableExecutionUtils.getPage(chats, pageable, () -> totalSize);
    }


    @Override
    public Long countByReceiverIdAndStatus(Long id, ChatStatus status) {
        JPAQuery<Long> query = jpaQueryFactory.select(chat.count())
                .from(chat)
                .where(
                        chat.receiver.id.eq(id).and(chat.status.eq(status))
                );
        return query.fetch().get(0);
    }

    /**
     * 주고 받은 메세지를 찾는 쿼리를 제작하는 메서드
     * 두 유저 서로가 서로의 수신자 혹은 발신자인 데이터를 찾는 쿼리
     */
    private <T> JPAQuery<T> buildQueryForSelectExchangedMessages(Expression<T> expr, Long roomId) {
        return jpaQueryFactory.select(expr)
                .from(chat)
                .where(
                        chat.room.id.eq(roomId)
                );
    }
}
