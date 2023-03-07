package com.sparta.sbug.thread.repository;

import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.query.ThreadQueryRepository;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ThreadRepository extends JpaRepository<Thread, Long>, ThreadQueryRepository {
    /**
     * 활성화된 쓰레드만 조회
     *
     * @param threadId
     * @return
     */
    Optional<Thread> findThreadByIdAndInUseIsTrue(Long threadId);

    /**
     * 대상 채널에 작성된 모든 쓰레드를 조회
     *
     * @param channelId : 대상 채널 ID
     * @param pageable  : 페이저블
     */
    @Query("select c from Thread c join fetch c.user u where c.id = :channelId")
    Slice<Thread> findThreadsByChannelIdAndInUseIsTrue(@Param("channelId") Long channelId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Thread t set t.inUse = false where t.id = :threadId")
    void disableThreadById(@Param("id") Long threadId);

    @Modifying(clearAutomatically = true)
    @Query("update Thread t set t.inUse = false where t.channel.id = :channelId")
    void disableThreadByChannelId(@Param("id") Long channelId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Thread t where t.inUse = false and t.modifiedAt <:localDateTime ")
    void deleteThreads(@Param("localDateTime") LocalDateTime localDateTime);

    boolean existsById(Long threadId);
}
