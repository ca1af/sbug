package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentAdminServiceImpl implements CommentAdminService{
    private final CommentRepository commentRepository;
    @Override
    public void disableComment(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("찾는 코멘트가 없습니다.")
        );
        commentRepository.disableCommentByCommentId(commentId);
    }
    @Override
    public void disableCommentByThreadId(Long threadId){
        commentRepository.disableCommentByThreadId(threadId);
    }
}
