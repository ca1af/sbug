package com.sparta.sbug.thread.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.comment.entity.QComment;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;
    private final ChannelServiceImpl channelService;
    private final JPAQueryFactory queryFactory;

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
        Channel channel = channelService.getChannelById(channelId);// getChannel() 매서드에서 exception 처리
        Thread thread = new Thread(channel,user, threadRequestDto.getContent());
        threadRepository.save(thread);
        return "Success";
    }

    //Thread 수정
    @Override
    @Transactional
    public String editThread(Long ThreadId, ThreadRequestDto threadRequestDto, User user){
        Thread thread = threadRepository.findByIdAndUser(ThreadId, user).orElseThrow(); // exception 처리
        thread.changeThread(threadRequestDto.getContent());
        threadRepository.save(thread);
        return "Success";
    }

    //Thread 삭제
    @Override
    @Transactional
    public String deleteThread(Long threadId, Long userId){
        Thread thread = getThread(threadId); // exception 처리
        threadRepository.delete(thread);
        return "Success";
    }

    @Override
    @Transactional(readOnly = true)
    public Thread findThreadById(Long id) {
        return threadRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long threadId) {
        Thread thread = threadRepository.findById(threadId).orElseThrow(
                () -> new IllegalArgumentException("쓰레드가 없습니다.")
        );
        QComment comment = QComment.comment;

        List<Comment> fetch = queryFactory
                .selectFrom(comment)
                .join(comment.thread)
                .on(comment.thread.id.eq(thread.getId()))
                .where(comment.thread.id.eq(thread.getId()))
                .fetch();
        return fetch.stream().map(CommentResponseDto::of).collect(Collectors.toList());
    }


}
