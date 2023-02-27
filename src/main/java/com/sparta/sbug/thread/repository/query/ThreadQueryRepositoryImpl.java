package com.sparta.sbug.thread.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.thread.dto.QThreadResponseDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.sparta.sbug.thread.entity.QThread.thread;

@Repository
public class ThreadQueryRepositoryImpl implements ThreadQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param jpaQueryFactory must not be {@literal null}.
     */

    public ThreadQueryRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
    private BooleanExpression searchByContent(String content){
        return Objects.nonNull(content) ? thread.content.contains(content) : null;
    }
    private BooleanExpression searchByEmail(String email){
        return Objects.nonNull(email) ? thread.user.email.contains(email) : null;
    }

    @Override
    public List<ThreadResponseDto> findThreadBySearchCondition(ThreadSearchCond threadSearchCond) {
        return jpaQueryFactory
                .select(new QThreadResponseDto(thread.id, thread.user.nickname, thread.user.id, thread.content, thread.createdAt, thread.modifiedAt))
//                .select(Projections.constructor(ThreadResponseDto.class, thread.id, thread.content, thread.user.id, thread.user.nickname, thread.createdAt, thread.emojis))
                .from(thread)
                .where(
                        searchByContent(threadSearchCond.getContent()),
                        searchByEmail(threadSearchCond.getEmail())
                )
                .fetch();
    }
}