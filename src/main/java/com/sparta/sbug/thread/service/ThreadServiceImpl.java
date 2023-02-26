package com.sparta.sbug.thread.service;


import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sparta.sbug.common.exceptions.ErrorCode.*;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;

    /**
     * 하위 레이어 데이터 서비스 - 코멘트 서비스
     */
    private final CommentService commentService;


    // CRUD

    @Override
    @Transactional
    public ThreadResponseDto createThread(Channel channel, String requestContent, User user) {
        Thread thread = Thread.builder()
                .requestContent(requestContent)
                .user(user)
                .channel(channel).build();
        Thread savedThread = threadRepository.save(thread);
        return ThreadResponseDto.of(savedThread);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<ThreadResponseDto> getAllThreadsInChannel(Long channelId, PageDto pageDto) {
        Slice<Thread> threads = threadRepository.findThreadsByChannelIdAndInUseIsTrue(channelId, pageDto.toPageable());
        return threads.map(ThreadResponseDto::of);
    }

    @Override
    @Transactional(readOnly = true)
    public ThreadResponseDto getThread(Long threadId) {
        Thread thread = findThreadById(threadId);
        ThreadResponseDto responseDto = ThreadResponseDto.of(thread);
        responseDto.setEmojis(thread.getEmojis().stream().map(EmojiResponseDto::of).collect(Collectors.toList()));
        return responseDto;
    }

    @Override
    @Transactional
    public ThreadResponseDto editThread(Long threadId, String requestContent, User user) {
        Thread thread = validateUserAuth(threadId, user);

        if (requestContent.trim().equals("")) {
            throw new CustomException(BAD_REQUEST_THREAD_CONTENT);
        }

        thread.updateThread(requestContent);
        return ThreadResponseDto.of(thread);
    }

    @Override
    @Transactional
    public void disableThread(Long threadId, User user) {
        validateUserAuth(threadId, user);
        commentService.disableCommentByThreadId(threadId);
        threadRepository.disableThreadById(threadId);
    }

    // 유저의 권한 검증
    @Override
    @Transactional
    public Thread validateUserAuth(Long threadId, User user) {
        Thread thread = findThreadById(threadId);
        if (!thread.getUser().getId().equals(user.getId())) {
            throw new CustomException(USER_THREAD_FORBIDDEN);
        }
        return thread;
    }

    // 쓰레드 데이터 조회
    @Override
    @Transactional(readOnly = true)
    public Thread findThreadById(Long threadId) {
        Optional<Thread> optionalThread = threadRepository.findThreadByIdAndInUseIsTrue(threadId);
        if (optionalThread.isEmpty()) {
            throw new CustomException(THREAD_NOT_FOUND);
        }
        return optionalThread.get();
    }

    // Disable by admin //
    @Override
    @Transactional
    public void disableThreadsByChannelId(Long channelId) {
        commentService.disableCommentByChannelId(channelId);
        threadRepository.disableThreadByChannelId(channelId);
    }

    @Override
    @Transactional
    public void disableThread(Long threadId) {
        commentService.disableCommentByThreadId(threadId);
        threadRepository.disableThreadById(threadId);
    }

    // Delete //
    @Override
    @Transactional
    @Scheduled(cron = "0 0 5 1 1/3 ? *")
    public void deleteThreadsOnSchedule() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(6);
        threadRepository.deleteThreads(localDateTime);
    }
}
