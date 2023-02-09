package com.sparta.sbug.thread.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;
    private final ChannelServiceImpl channelService;

    // Thread 찾는 메소드
    @Override
    @Transactional
    public Thread getThread(Long threadId) {
        return threadRepository.findById(threadId).orElseThrow(
                () -> new IllegalArgumentException("쓰레드가 없습니다")
        );
    }

    //Thread 생성
    @Override
    @Transactional
    public String createThread(Long channelId, ThreadRequestDto threadRequestDto, User user) {
        Channel channel = channelService.getChannel(channelId);// getChannel() 매서드에서 exception 처리
        Thread thread = new Thread(channel,user, threadRequestDto.getContent());
        threadRepository.save(thread);
        return "channelName/thread/{id}";
    }

    //Thread 수정
    @Override
    @Transactional
    public String editThread(Long ThreadId, ThreadRequestDto threadRequestDto, User user){
        Thread thread = threadRepository.findByIdAndUser(ThreadId, user).orElseThrow(); // exception 처리
        thread.changeThread(threadRequestDto.getContent());
        threadRepository.save(thread);
        return "channelName/thread/{id}";
    }

    //Thread 삭제
    @Override
    @Transactional
    public String deleteThread(Long threadId, User user){
        Thread thread = threadRepository.findByIdAndUser(threadId, user).orElseThrow(); // exception 처리
        threadRepository.delete(thread);
        return "channelName/home";
    }

    @Override
    @Transactional(readOnly = true)
    public Thread findThreadById(Long id) {
        return threadRepository.findById(id).orElseThrow();
    }

}
