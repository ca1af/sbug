package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// lombok
@RequiredArgsConstructor

// springframework
@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    //    private final JPAQueryFactory queryFactory;
    private final ThreadService threadService;
    private final UserChannelService userChannelService;

    @Override
    @Transactional(readOnly = true)
    public Channel getChannelById(Long channelId) {
        return channelRepository.findById(channelId).orElseThrow(
                () -> new IllegalArgumentException("채널이 없습니다")
        );
    }

    @Override
    @Transactional
    public Channel createChannel(User user, String channelName) {
        Channel channel = Channel.builder().adminEmail(user.getEmail()).channelName(channelName).build();
        return channelRepository.save(channel);
    }

    @Override
    @Transactional
    public void updateChannelName(Long channelId, User user, String channelName) {
        Channel channel = getChannelById(channelId);
        validateUserIsChannelAdmin(channel, user);
        channel.updateChannelName(channelName);
    }

    @Override
    @Transactional
    public void deleteChannel(Long channelId, User user) {
        Channel channel = getChannelById(channelId);
        validateUserIsChannelAdmin(channel, user);
        channelRepository.delete(channel);
    }

    private static void validateUserIsChannelAdmin(Channel channel, User user) {
        if (!channel.getAdminEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("채널 관리자만 수정 할 수 있습니다.");
        }
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<ThreadResponseDto> getThreads(Long id) {
//        Channel channel = channelRepository.findById(id).orElseThrow(
//                () -> new IllegalArgumentException("채널이 없습니다.")
//        );
//        QThread thread = QThread.thread;
//
//        List<Thread> fetch = queryFactory
//                .selectFrom(thread)
//                .join(thread.channel)
//                .on(thread.channel.id.eq(channel.getId()))
//                .where(thread.channel.id.eq(channel.getId()))
//                .fetch();
//        return fetch.stream().map(ThreadResponseDto::of).collect(Collectors.toList());
//    }
    // threadService 쪽에서 getAllThread 만들어서
    // threadService 를 의존하는 형태로 가야 할 것 같아요. <



    // Thread 생성
    @Override
    @Transactional
    public String createThread(Long channelId, String requestContent, User user) {
        Channel channel = getChannelById(channelId);
        if (!userChannelService.isUserJoinedByChannel(user, channel)) {
            throw new IllegalArgumentException("유저가 채널에 속해있지 않습니다. 쓰레드 생성권한이 없습니다.");
        }
        return threadService.createThread(channel, requestContent, user);
    }

    // Thread 수정
    @Override
    @Transactional
    public String editThread(Long threadId, String requestContent, User user){
        Thread thread = validateUserAuth(threadId, user);
        return threadService.editThread(thread, requestContent);
    }

    // Thread 삭제
    @Override
    @Transactional
    public String deleteThread(Long threadId, User user){
        Thread thread = validateUserAuth(threadId, user);
        return threadService.deleteThread(thread);
    }

    @Transactional
    public Thread validateUserAuth(Long threadId, User user){
        Thread thread = threadService.getThread(threadId);
        if (!thread.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정할 수 있는 권한이 없습니다.");
        }
        return thread;
    }
}
