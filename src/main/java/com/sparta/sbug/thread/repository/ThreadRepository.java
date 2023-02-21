package com.sparta.sbug.thread.repository;

import com.sparta.sbug.thread.entity.Thread;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    /**
     * 활성화된 쓰레드만 조회
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

    Slice<Thread> findThreadsByChannelIdAndInUseIsTrue(Long channelId, Pageable pageable);
//    @Query("update Thread t set t.inUse = false where t.id = :threadId")
    @Query(nativeQuery = true, value = "update thread set in_use =true where id =:threadId")
    @Modifying(clearAutomatically = true)
    void disableThreadById(@Param("id") Long threadId);

//    @Query("update Thread t set t.inUse = false where t.channel.id = :channelId")
    @Query(nativeQuery = true, value = "update thread set in_use = false where channel_id =:channelId")
    @Modifying(clearAutomatically = true)
    void disableThreadByChannelId(@Param("id") Long channelId);

    @Modifying(clearAutomatically = true)
//    @Query("delete from Thread t where t.inUse = false and t.modifiedAt < :localDateTime ")
    @Query(nativeQuery = true, value = "delete from thread where in_use = false and modified_at <:localDateTime")
    void deleteThreads(@Param("localDateTime") LocalDateTime localDateTime);
}
