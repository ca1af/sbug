package com.sparta.sbug.chat.repository;

import static com.sparta.sbug.chat.entity.QChat.chat;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.chat.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;


@RequiredArgsConstructor
public class ChatRepositoryQueryImpl implements ChatRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Chat> findExchangedMessages(Long userId1, Long userId2, Pageable pageable) {
        JPAQuery<Chat> query = buildQueryForSelectExchangedMessages(chat, userId1, userId2)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = buildQueryForSelectExchangedMessages(Wildcard.count, userId1, userId2);

        List<Chat> chats = query.fetch();
        long totalSize = countQuery.fetch().get(0);

        return PageableExecutionUtils.getPage(chats, pageable, () -> totalSize);
    }

    private <T> JPAQuery<T> buildQueryForSelectExchangedMessages(Expression<T> expr, Long userId1, Long userId2) {
        return jpaQueryFactory.select(expr)
                .from(chat)
                .where(
                        chat.receiver.id.eq(userId1), (chat.sender.id.eq(userId2)).or(
                                chat.receiver.id.eq(userId2).and(chat.sender.id.eq(userId1))
                        )
                );
    }
}
