package com.sparta.sbug.chatroom.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static com.sparta.sbug.chatroom.entity.QChatRoom.chatRoom;
import static com.sparta.sbug.userchatroom.entity.QUserChatRoom.userChatRoom;

import com.sparta.sbug.chatroom.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ChatRoomRepositoryQueryImpl implements ChatRoomRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ChatRoom> findAllByUserId(Long myUserId, Pageable pageable) {
        JPAQuery<ChatRoom> query = buildQueryForSelectAllMyChatRoom(chatRoom, myUserId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        JPAQuery<Long> countQuery = buildQueryForSelectAllMyChatRoom(Wildcard.count, myUserId);

        List<ChatRoom> chatRooms = query.fetch();
        long totalSize = countQuery.fetch().get(0);

        return PageableExecutionUtils.getPage(chatRooms, pageable, () -> totalSize);
    }

    @Override
    public Optional<ChatRoom> findByUsersId(Long myUserId, Long theOtherUserId) {
        JPAQuery<ChatRoom> query = jpaQueryFactory.select(chatRoom)
                .from(chatRoom)
                .where(
                        chatRoom.id.in(
                                JPAExpressions.select(chatRoom.id)
                                        .from(chatRoom)
                                        .leftJoin(chatRoom.userChatRooms, userChatRoom)
                                        .on(
                                                userChatRoom.user.id.eq(myUserId)
                                        )
                        )
                );

        List<ChatRoom> chatRooms = query.fetch();
        return Optional.of(chatRooms.get(0));
    }

    private <T> JPAQuery<T> buildQueryForSelectAllMyChatRoom(Expression<T> expr, Long myUserId) {
        return jpaQueryFactory.select(expr)
                .from(chatRoom)
                .leftJoin(chatRoom.userChatRooms, userChatRoom)
                .on(
                        userChatRoom.user.id.eq(myUserId)
                );
    }
}
