package com.sparta.sbug.upperlayerservice;

import com.sparta.sbug.user.entity.User;

public interface AdminUpperLayerService {
    // 채널을 비활성화 + 중간 테이블인 유저채널 테이블도 비활성화 하는 로직.
    void disableChannelAndDependentUserChannel(Long channelId);

    // 쓰레드를 비활성화 하는 로직
    void disableThread(Long threadId);
    // 댓글을 비활성화 하는 로직

    void disableComment(Long commentId);
}
