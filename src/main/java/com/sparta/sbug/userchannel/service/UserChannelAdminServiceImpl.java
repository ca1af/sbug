package com.sparta.sbug.userchannel.service;

import com.sparta.sbug.userchannel.repository.UserChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserChannelAdminServiceImpl implements UserChannelAdminService{
    private final UserChannelRepository userChannelRepository;


    // 2. 채널에서 유저가 강퇴당한 경우
    @Override
    public void kickUser(Long userId) {
        userChannelRepository.disableAllUserChannelByUserIdAndInUse(userId);
    }
    // 3. 채널이 없어진 경우
    @Override
    public void disableUserChannelByChannelAbsence(Long channelId) {
        userChannelRepository.disableAllUserChannelByChannelIdAndInUse(channelId);
    }

}
