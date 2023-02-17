package com.sparta.sbug.userchannel.service;

public interface UserChannelAdminService {
    // 채널과 유저의 관계를 끊어주는 서비스



    // 2. 채널에서 유저가 강퇴당한 경우
    void kickUser(Long userId);

    // 3. 채널이 없어진 경우
    void disableUserChannelByChannelAbsence(Long channelId);


}
