package com.sparta.sbug.comment.repository;


import com.sparta.sbug.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

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
//    @Query("update Comment c set c.inUse = false where c.id = :commentId")
    @Query(value = "update comment set in_use = false where id =:commentId", nativeQuery = true)
    void disableCommentByCommentId(@Param("id") Long commentId);

    @Modifying(clearAutomatically = true)
//    @Query("update Comment c set c.inUse = false where c.thread.id = :threadId")
    @Query(value = "update comment set in_use = false where thread_id =:threadId", nativeQuery = true)
    void disableCommentByThreadId(@Param("id") Long threadId);

    @Modifying(clearAutomatically = true)
//    @Query("update Comment c set c.inUse = false where c.channel.id = :channelId")
    @Query(value = "update comment set in_use = false where channel_id =:channelId", nativeQuery = true)
    void disableCommentByChannelId(@Param("id") Long channelId);

    @Modifying(clearAutomatically = true)
//    @Query("delete from Comment c where c.inUse = false and c.modifiedAt < :localDateTime ")
    @Query(value = "delete from comment where in_use = false and modified_at <:localDateTime", nativeQuery = true)
    void deleteComments(@Param("localDateTime")LocalDateTime localDateTime);
    boolean existsById(Long commentId);
}
