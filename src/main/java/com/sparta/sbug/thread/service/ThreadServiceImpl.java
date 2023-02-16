package com.sparta.sbug.thread.service;


import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;

    @Override
    @Transactional(readOnly = true)
    public Thread getThread(Long threadId) {
        Optional<Thread> optionalThread = threadRepository.findById(threadId);
        if (optionalThread.isEmpty()) {
            throw new NoSuchElementException("쓰레드를 찾을 수 없습니다.");
        }
        return optionalThread.get();
    }

    @Override
    @Transactional
    public void createThread(Channel channel, String requestContent, User user) {
        Thread thread = Thread.builder()
                .requestContent(requestContent)
                .user(user)
                .channel(channel).build();
        threadRepository.save(thread);
    }

    @Override
    @Transactional
    public void editThread(Long threadId, String requestContent, User user) {
        Thread thread = validateUserAuth(threadId, user);
        thread.updateThread(requestContent);
    }

    @Override
    @Transactional
    public void deleteThread(Long threadId, User user) {
        Thread thread = validateUserAuth(threadId, user);
        threadRepository.delete(thread);
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
        Thread thread = getThread(threadId);
        if (!thread.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        return thread;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadResponseDto> getAllThreadsInChannel(Long channelId, PageDto pageDto) {
        Page<Thread> threadPages = threadRepository.findThreadsByChannelId(channelId, pageDto.toPageable());
        List<Thread> threads = threadPages.getContent();
        return threads.stream().map(ThreadResponseDto::of).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Thread findThreadById(Long id) {
        return threadRepository.findById(id).orElseThrow();
    }

}
