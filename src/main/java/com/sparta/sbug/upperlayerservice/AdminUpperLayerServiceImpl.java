package com.sparta.sbug.upperlayerservice;

import com.sparta.sbug.channel.service.ChannelAdminService;
import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.comment.service.CommentAdminService;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.thread.service.ThreadAdminService;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminUpperLayerServiceImpl implements AdminUpperLayerService{
    private final ChannelAdminService channelService;
    private final UserChannelService userChannelService;
    private final ThreadAdminService threadService;
    private final CommentAdminService commentService;
    @Override
    public void disableChannelAndDependentUserChannel(Long channelId) {
        // thread 개수만큼 thread 비활성화 쿼리가 나가야 하나? <
        commentService.disableCommentByChannelId(channelId);
        threadService.disableThreadsByChannelId(channelId);
        channelService.disableChannel(channelId);
    }

    @Override
    public void disableThread(Long threadId) {
        commentService.disableCommentByThreadId(threadId);
        // 채널 비활성화인데, 내가 쓴 글 보기 하면
        // 채널부터 조회쿼리 날리기. -> 활성화인가? 체크해서 아니면 안보여주기.
        threadService.disableThread(threadId);
    }

    @Override
    public void disableComment(Long commentId) {
        commentService.disableComment(commentId);
    }
}
