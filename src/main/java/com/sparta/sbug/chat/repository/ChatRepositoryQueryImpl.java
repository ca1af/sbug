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

    /**
     * 두 유저가 주고 받는 메세지를 찾는 메서드
     * Page 객체로 반환하기 위해 데이터를 불러오는 메서드와 함께 전체 데이터 개수를 세는 쿼리가 한 번 더 실행됩니다.
     */
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

    /**
     * 특정 유저가 자신에게 온 아직 읽지 않은 메세지들의 개수를 구하는 메서드
     */
    @Override
    public Long countByReceiverIdAndStatus(Long id, ChatStatus status, Pageable pageable) {
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
    private <T> JPAQuery<T> buildQueryForSelectExchangedMessages(Expression<T> expr, Long userId1, Long userId2) {
        return jpaQueryFactory.select(expr)
                .from(chat)
                .where(
                        (chat.receiver.id.eq(userId1).and(chat.sender.id.eq(userId2))).or(
                                chat.receiver.id.eq(userId2).and(chat.sender.id.eq(userId1))
                        )
                );
    }
}
