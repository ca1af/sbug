package com.sparta.sbug.channel.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.QThread;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserServiceImpl;
import com.sparta.sbug.userchannel.enttiy.UserChannel;
import com.sparta.sbug.userchannel.repository.UserChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserServiceImpl userService;
    private final JPAQueryFactory queryFactory;
    private final UserChannelRepository userChannelRepository;

    @Override
    @Transactional(readOnly = true)
    public Channel getChannel(Long channelId) {
        return channelRepository.findById(channelId).orElseThrow(
                () -> new IllegalArgumentException("채널이 없습니다")
        );
    }

    @Override
    public void createChannel(User user, String channelName) {
        // 패러미터 유저 1번 셀렉쿼리
        Channel channel = Channel.builder().adminEmail(user.getEmail()).channelName(channelName).build();
        UserChannel userChannel = UserChannel.builder().user(user).channel(channel).build();

        channelRepository.save(channel);
        userChannelRepository.save(userChannel);
    }

    @Override
    public String inviteUser(User user, Long id, String email) {
        User requester = userService.getUser(email);
        Channel channel = getChannel(id);

        UserChannel userChannel = UserChannel.builder().user(requester).channel(channel).build();
        userChannelRepository.save(userChannel);

        return "added";
    }

    private static void channelAdminChecker(Channel channel, User user) {
        if(!channel.getAdminEmail().equals(user.getEmail())){
            throw new IllegalArgumentException("채널 관리자만 수정 할 수 있습니다.");
        }
    }
    @Override
    public void updateChannelName(Long id, User user, String channelName) {
        Channel channel = getChannel(id);
        channelAdminChecker(channel,user);
        channel.updateChannel(channel,user,channelName);
    }
    @Override
    public Long deleteChannel(User user, Long id) {
        Channel channel = channelRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("찾는 채널이 없습니다.")
        );
        // 상위 서비스에서 딜리트 호출하고, UserChannel 도 삭제. <
        channelAdminChecker(channel,user);
        channelRepository.delete(channel);
        return channel.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadResponseDto> getThreads(Long id) {
        Channel channel = channelRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("채널이 없습니다.")
        );
        QThread thread = QThread.thread;

        List<Thread> fetch = queryFactory
                .selectFrom(thread)
                .join(thread.channel)
                .on(thread.channel.id.eq(channel.getId()))
                .where(thread.channel.id.eq(channel.getId()))
                .fetch();
        return fetch.stream().map(ThreadResponseDto::of).collect(Collectors.toList());
    }
    // threadService 쪽에서 getAllThread 만들어서
    // threadService 를 의존하는 형태로 가야 할 것 같아요. <
}
