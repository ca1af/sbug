package com.sparta.sbug.thread.repository.query;

import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ThreadQueryRepositoryImplTest {
    @Autowired
    ThreadQueryRepositoryImpl threadQueryRepository;

    @Test
    void findThreadBySearchCondition() {

        ThreadSearchCond threadSearchCond = ThreadSearchCond.builder().email("user1@naver.com").build();

        List<ThreadResponseDto> threadBySearchCondition = threadQueryRepository.findThreadBySearchCondition(threadSearchCond);

    }
}