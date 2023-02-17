package com.sparta.sbug.comment.repository;


import com.sparta.sbug.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 쓰레드 ID를 이용해서
     *
     * @param threadId 쓰레드 ID
     * @param pageable 페이징 정보
     * @return Page&lt;Comment&gt;
     */
    Page<Comment> findCommentsByThreadIdAndInUseIsTrue(Long threadId, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Comment c set c.inUse = false where c.id = :commentId")
    void disableCommentByCommentId(@Param("id") Long commentId);

    @Modifying(clearAutomatically = true)
    @Query("update Comment c set c.inUse = false where c.thread.id = :threadId")
    void disableCommentByThreadId(@Param("id") Long threadId);

    @Modifying(clearAutomatically = true)
    @Query("update Comment c set c.inUse = false where c.channel.id = :channelId")
    void disableCommentByChannelId(@Param("id") Long channelId);
}
