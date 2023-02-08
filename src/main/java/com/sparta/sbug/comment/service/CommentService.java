package com.sparta.sbug.comment.service;

import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;

import java.util.List;

public interface CommentService {

    List<CommentResponseDto> getAllCommentsInThread(Long threadId, PageDto pageDto);

    String createComment(Thread thread, String content, User user);

    String updateComment(Long commentId, String content, Long userId);

    String deleteComment(Long commentId, Long userId);
}
