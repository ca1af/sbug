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


@Service
@RequiredArgsConstructor
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

    //Thread 생성
    @Override
    @Transactional
    public String createThread(Channel channel, String requestContent, User user) {
        Thread thread = Thread.builder()
                .requestContent(requestContent)
                .user(user)
                .channel(channel).build();
        threadRepository.save(thread);
        return "Success";
    }

    //Thread 수정
//    @Override
//    @Transactional
//    public String editThread(Thread thread, String requestContent){
//        thread.updateThread(requestContent);
//        return "Success";
//    }

    // Thread 수정
    @Override
    @Transactional
    public String editThread(Long threadId, String requestContent, User user){
        Thread thread = validateUserAuth(threadId, user);
        thread.updateThread(requestContent);
        threadRepository.save(thread);
        return "Success";
    }

    //Thread 삭제
//    @Override
//    @Transactional
//    public String deleteThread(Thread thread){
//        threadRepository.delete(thread);
//        return "Success";
//    }


    @Override
    @Transactional
    public String deleteThread(Long threadId, User user) {
        Thread thread = validateUserAuth(threadId, user);
        threadRepository.delete(thread);
        return "Success";
    }

    @Transactional
    public Thread validateUserAuth(Long threadId, User user) {
        Thread thread = getThread(threadId);
        if (!thread.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정할 수 있는 권한이 없습니다.");
        }
        return thread;
    }


    @Override
    @Transactional(readOnly = true)
    public List<ThreadResponseDto> getAllThreadsInChannel(Long channelId, PageDto pageDto){
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
