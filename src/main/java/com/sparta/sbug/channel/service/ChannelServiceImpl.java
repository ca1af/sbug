package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

// lombok
@RequiredArgsConstructor

// springframework
@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    //    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public Channel getChannelById(Long channelId) {
        return channelRepository.findById(channelId).orElseThrow(
                () -> new NoSuchElementException("채널이 없습니다")
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
        channel.updateChannel(channel, user, channelName);
    }

    @Override
    @Transactional
    public void deleteChannel(User user, Long channelId) {
        Channel channel = getChannelById(channelId);
        // 상위 서비스에서 딜리트 호출하고, UserChannel 도 삭제. <
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
}
