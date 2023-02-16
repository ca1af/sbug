package com.sparta.sbug.comment.repository;


import com.sparta.sbug.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 쓰레드 ID를 이용해서
     *
     * @param threadId 쓰레드 ID
     * @param pageable 페이징 정보
     * @return Page&lt;Comment&gt;
     */
    Page<Comment> findCommentsByThreadId(Long threadId, Pageable pageable);
}
