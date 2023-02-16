package com.sparta.sbug.thread.repository;

import com.sparta.sbug.thread.entity.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    /**
     * 대상 채널에 작성된 모든 쓰레드를 조회
     *
     * @param channelId : 대상 채널 ID
     * @param pageable  : 페이저블
     */
    Page<Thread> findThreadsByChannelId(Long channelId, Pageable pageable);
}
