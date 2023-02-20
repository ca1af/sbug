package com.sparta.sbug.thread.service;


import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sparta.sbug.common.exceptions.ErrorCode.BAD_REQUEST_THREAD_CONTENT;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class ThreadServiceImpl implements ThreadService {


    private final ThreadRepository threadRepository;

    @Override
    @Transactional(readOnly = true)
    public Thread findThreadById(Long threadId) {
        Optional<Thread> optionalThread = threadRepository.findById(threadId);
        if (optionalThread.isEmpty()) {
            throw new NoSuchElementException("쓰레드를 찾을 수 없습니다.");
        }
        return optionalThread.get();
    }

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
    @Transactional
    public ThreadResponseDto editThread(Long threadId, String requestContent, User user) {
        if (requestContent.trim().equals("")) {
            throw new CustomException(BAD_REQUEST_THREAD_CONTENT);
        }

        Thread thread = validateUserAuth(threadId, user);
        thread.updateThread(requestContent);
        return ThreadResponseDto.of(thread);
    }

    @Override
    @Transactional
    public void disableThread(Long threadId, User user) {
        validateUserAuth(threadId, user);
        threadRepository.disableThreadById(threadId);
    }

    /**
     * 요청자가 대상 쓰레드를 수정 혹은 삭제할 수 있는 권한이 있는지 확인합니다.
     *
     * @param threadId 대상 쓰레드 ID
     * @param user     요청자
     * @return Thread
     */
    @Transactional
    public Thread validateUserAuth(Long threadId, User user) {
        Thread thread = findThreadById(threadId);
        if (!thread.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        return thread;
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

    @Transactional
    @Override
    public void autoDelete(){
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(6);
        threadRepository.deleteThreads(localDateTime);
    }
}
