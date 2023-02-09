package com.sparta.sbug.channel.service;

import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchannel.repository.UserChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class CascadeService {
    private final ChannelServiceImpl channelService;
    private final UserChannelRepository userChannelRepository;
    @Transactional
    public void cascadeDelete(User user, Long id){
        Long channelId = channelService.deleteChannel(user, id);
        userChannelRepository.deleteAllByChannelId(channelId);
    }
}
