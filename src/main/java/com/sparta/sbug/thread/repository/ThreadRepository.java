package com.sparta.sbug.thread.repository;

import com.sparta.sbug.thread.entity.Thread;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    /**
     * 대상 채널에 작성된 모든 쓰레드를 조회
     *
     * @param channelId : 대상 채널 ID
     * @param pageable  : 페이저블
     */
    Page<Thread> findThreadsByChannelIdAndInUseIsTrue(Long channelId, Pageable pageable);
    @Query("update Thread t set t.inUse = false where t.id = :threadId")
    @Modifying(clearAutomatically = true)
    void disableThreadById(@Param("id") Long threadId);

    @Query("update Thread t set t.inUse = false where t.channel.id = :channelId")
    @Modifying(clearAutomatically = true)
    void disableThreadByChannelId(@Param("id") Long channelId);
}
