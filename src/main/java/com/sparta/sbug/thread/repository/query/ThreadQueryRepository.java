package com.sparta.sbug.thread.repository.query;

import com.sparta.sbug.thread.dto.ThreadResponseDto;

import java.util.List;

public interface ThreadQueryRepository {
    List<ThreadResponseDto> findThreadBySearchCondition(ThreadSearchCond threadSearchCond);
}