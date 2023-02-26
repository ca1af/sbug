package com.sparta.sbug.comment.service;

import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.repository.CommentRepository;
import com.sparta.sbug.thread.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentAdminServiceImpl implements CommentAdminService{
    private final CommentRepository commentRepository;

    @Override
    public void disableComment(Long commentId) {
        commentRepository.disableCommentByCommentId(commentId);
    }
    @Override
    public void disableCommentByThreadId(Long threadId){
        commentRepository.disableCommentByThreadId(threadId);
    }

    @Override
    public void disableCommentByChannelId(Long channelId) {
        commentRepository.disableCommentByChannelId(channelId);
    }
}