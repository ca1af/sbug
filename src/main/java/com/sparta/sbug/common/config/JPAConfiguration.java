package com.sparta.sbug.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// springframework context
@Configuration
public class JPAConfiguration {

    /**
     * EntityManager를 빈으로 주입
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * "JPAQueryFactory"를  빈으로 주입
     *
     * @return JPAQueryFactory
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
