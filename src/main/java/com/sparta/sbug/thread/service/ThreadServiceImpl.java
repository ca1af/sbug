package com.sparta.sbug.thread.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;
    private final ChannelServiceImpl channelService;


    //Thread 생성
    @Override
    @Transactional
    public String creatThread(Long channelId, ThreadRequestDto threadRequestDto, User user) {
        Channel channel = channelService.getChannel(channelId);
        // exception 처리 할 필요 없습니다. getChannel 매서드에서 이미 처리됩니다.
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
        Thread thread = threadRepository.findByIdAndUser(threadId, user).orElseThrow(); // exception처리
        threadRepository.delete(thread);
        return "channelName/home";
    }

}
