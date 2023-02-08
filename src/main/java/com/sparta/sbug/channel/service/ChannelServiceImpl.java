package com.sparta.sbug.channel.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.sbug.channel.dto.ChannelRequestDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.entity.QChannel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.entity.QThread;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserServiceImpl userService;
    private final JPAQueryFactory queryFactory;

    @Override
    public Channel getChannel(Long channelId) {
        return channelRepository.findById(channelId).orElseThrow(
                () -> new IllegalArgumentException("채널이 없습니다")
        );
    }

    @Override
    public String createChannel(User user, ChannelRequestDto dto) {
        Channel channel = Channel.builder().user(user).adminEmail(user.getEmail()).channelName(dto.getChannelName()).build();
        channelRepository.save(channel);
        user.addChannel(channel);
        return "created";
    }

    @Override
    public String inviteUser(User user, Channel channel, String email) {
        User requester = userService.getUser(email).orElseThrow(
                () -> new IllegalArgumentException("유저가 없습니다")
        );
        // 초대할 사람 객체입니다(이메일로 찾습니다)
        Set<Channel> channels = user.getChannels();
        // 초대를 보낸 유저가 가진 채널들입니다.
        if(channels.contains(channel)){
            // 초대를 보낸 유저가 가진 채널 중 초대할 채널이 있는지 확인합니다.
            requester.addChannel(channel);
            // 그 후 초대할 사람의 채널 목록에 채널을 추가합니다.
        }
        return "added";
    }

    private static void channelAdminChecker(Channel channel, User user) {
        if(!channel.getAdminEmail().equals(user.getEmail())){
            throw new IllegalArgumentException("채널 관리자만 수정 할 수 있습니다.");
        }
    }
    @Override
    public void updateChannelName(Channel channel, User user, ChannelRequestDto dto) {
        channelAdminChecker(channel,user);
        channel.updateChannel(channel,user,dto);
    }
    @Override
    public void deleteChannel(Channel channel, User user) {
        channelAdminChecker(channel,user);
        channelRepository.delete(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThreadResponseDto> getThreads(Channel channel) {
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
