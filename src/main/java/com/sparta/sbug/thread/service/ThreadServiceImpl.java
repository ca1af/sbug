package com.sparta.sbug.thread.service;


import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.emoji.dto.EmojiCountDto;
import com.sparta.sbug.emoji.dto.EmojiResponseDto;
import com.sparta.sbug.emoji.service.ThreadEmojiService;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.repository.ThreadRepository;
import com.sparta.sbug.thread.repository.query.ThreadSearchCond;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.cache.CacheNames;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import java.time.LocalDateTime;
import java.util.*;

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

    /**
     * 하위 레이어 데이터 서비스 - 쓰레드 이모지 서비스
     */
    private final ThreadEmojiService threadEmojiService;


    // CRUD

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.THREADSINCHANNEL, key = "#channel.id")
    public ThreadResponseDto createThread(Channel channel, String requestContent, User user) {
        Thread thread = Thread.builder()
                .requestContent(requestContent)
                .user(user)
                .build();
        thread.setChannel(channel);
        Thread savedThread = threadRepository.save(thread);
        return ThreadResponseDto.of(savedThread, null);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = CacheNames.THREADSINCHANNEL, key = "#channelId")
    public Slice<ThreadResponseDto> getAllThreadsInChannel(Long channelId, PageDto pageDto) {
        Slice<Thread> threads = threadRepository.findThreadsByChannelIdAndInUseIsTrue(channelId, pageDto.toPageable());
        List<Long> threadIds = threads.getContent().stream().map(Thread::getId).toList();
        List<EmojiCountDto> emojiCountDtoList = threadEmojiService.getThreadsEmojiCount(threadIds);
        Map<Long, List<EmojiResponseDto>> threadEmojiCountMap = EmojiResponseDto.getEmojiCountMap(emojiCountDtoList);
        return threads.map(thread -> ThreadResponseDto.of(thread, threadEmojiCountMap));
    }

    @Override
    @Transactional(readOnly = true)
    public ThreadResponseDto getThread(Long threadId) {
        Thread thread = findThreadById(threadId);
        List<EmojiCountDto> emojiCountDtoList = threadEmojiService.getThreadEmojiCount(threadId);
        Map<Long, List<EmojiResponseDto>> threadEmojiCountMap = EmojiResponseDto.getEmojiCountMap(emojiCountDtoList);
        return ThreadResponseDto.of(thread, threadEmojiCountMap);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.THREAD, key = "#threadId")
    public void editThread(Long threadId, String requestContent, User user) {
        Thread thread = validateUserAuth(threadId, user);

        if (requestContent.trim().equals("")) {
            throw new CustomException(BAD_REQUEST_THREAD_CONTENT);
        }

        thread.updateThread(requestContent);
    }

    //THREADSINCHANNEL도 evict 해줘야 할것 같은데, channelId를 가져올 수 없음
    @Override
    @Transactional
    @CacheEvict(cacheNames = CacheNames.THREAD, key = "#threadId")
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
    @Cacheable(cacheNames = CacheNames.THREAD, key = "#threadId")
    public Thread findThreadById(Long threadId) {
        Optional<Thread> optionalThread = threadRepository.findThreadByIdAndInUseIsTrue(threadId);
        if (optionalThread.isEmpty()) {
            throw new CustomException(THREAD_NOT_FOUND);
        }
        return optionalThread.get();
    }

    // 코멘트 생성
    @Override
    @Transactional
    public CommentResponseDto createComment(Long threadId, String content, User user) {
        Thread thread = findThreadById(threadId);
        return commentService.createComment(thread, content, user);
    }

    // Disable by admin //
    @Override
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
    @Scheduled(cron = "0 0 5 1 3,6,9,12 *")
    public void deleteThreadsOnSchedule() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(6);
        threadRepository.deleteThreads(localDateTime);
    }

    @Override
    public boolean reactThreadEmoji(String emojiType, User user, Long threadId) {
        Thread thread = findThreadById(threadId);
        return threadEmojiService.reactThreadEmoji(emojiType, user, thread);
    }

    @Override
    public List<ThreadResponseDto> findThreadBySearchCondition(ThreadSearchCond threadSearchCond){
        return threadRepository.findThreadBySearchCondition(threadSearchCond);
    }
}
