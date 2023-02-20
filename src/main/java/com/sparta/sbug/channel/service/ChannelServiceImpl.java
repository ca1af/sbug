package com.sparta.sbug.channel.service;

import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.repository.ChannelRepository;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.sbug.common.exceptions.ErrorCode.BAD_REQUEST_THREAD_CONTENT;
import static com.sparta.sbug.common.exceptions.ErrorCode.USER_CHANNEL_FORBIDDEN;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
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
    @Transactional(readOnly = true)
    public Slice<ChannelResponseDto> getChannels(PageDto pageDto) {
        Slice<Channel> channels = channelRepository.findAllByInUseIsTrue(pageDto.toPageable());
        return channels.map(ChannelResponseDto::of);
    }


    @Override
    @Transactional
    public Channel createChannel( String channelName) {
        Channel channel = Channel.builder().channelName(channelName).build();
        return channelRepository.save(channel);
    }

    @Override
    @Transactional
    public void updateChannelName(Long channelId, User user, String channelName) {
        Channel channel = getChannelById(channelId);
        channel.updateChannelName(channelName);
    }

    @Override
    @Transactional
    public void deleteChannel(Long channelId, User user) {
        Channel channel = getChannelById(channelId);
        channelRepository.delete(channel);
    }

//    private static void validateUserIsChannelAdmin(Channel channel, User user) {
//        if (!channel.getAdminEmail().equals(user.getEmail())) {
//            throw new IllegalArgumentException("채널 관리자만 수정 할 수 있습니다.");
//        }
//    }

    // Thread 생성
    @Override
    @Transactional
    public ThreadResponseDto createThread(Long channelId, String requestContent, User user) {
        if (requestContent.trim().equals("")) {
            throw new CustomException(BAD_REQUEST_THREAD_CONTENT);
        }

        Channel channel = validateUserInChannel(channelId, user);
        return threadService.createThread(channel, requestContent, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Channel validateUserInChannel(Long channelId, User user) {
        if (!userChannelService.isUserJoinedByChannel(user, channelId)) {
            throw new CustomException(USER_CHANNEL_FORBIDDEN);
        }
        
        return getChannelById(channelId);
    }
}
