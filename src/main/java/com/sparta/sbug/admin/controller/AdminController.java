package com.sparta.sbug.admin.controller;

import com.sparta.sbug.admin.dto.AdminResponseDto;
import com.sparta.sbug.admin.service.AdminService;
import com.sparta.sbug.channel.dto.ChannelDto;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.service.ChannelService;
import com.sparta.sbug.comment.dto.CommentResponseDto;
import com.sparta.sbug.comment.service.CommentService;
import com.sparta.sbug.common.paging.PageDto;
import com.sparta.sbug.security.dto.TokenResponseDto;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.thread.service.ThreadService;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.userchannel.service.UserChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    private final JwtProvider jwtProvider;

    /**
     * 하위 레이어 데이터 서비스 - 채널 서비스
     */
    private final ChannelService channelService;

    /**
     * 하위 레이어 데이터 서비스 - 채널 서비스
     */
    private final UserChannelService userChannelService;

    /**
     * 하위 레이어 데이터 서비스 - 쓰레드 서비스
     */
    private final ThreadService threadService;

    /**
     * 하위 레이어 데이터 서비스 - 코멘트 서비스
     */
    private final CommentService commentService;


    // Login //

    /**
     * 관리자 로그인
     *
     * @param requestDto 로그인 요청 정보 ( 이메일, 패스워드 )
     * @return TokenResponseDto ( 액세스 토큰, 리프레쉬 토큰 )
     */
    @PostMapping("/login")
    public TokenResponseDto loginAdmin(@RequestBody LoginRequestDto requestDto) {
        String infoLog = "[POST] /api/admins/login";
        log.info(infoLog);

        AdminResponseDto responseDto = adminService.loginAdmin(requestDto.getEmail(), requestDto.getPassword());
        return jwtProvider.createTokenAdmin(responseDto.getEmail());
    }


    // Inquiry ( 조회 ) //

    /**
     * 모든 채널들을 조회
     *
     * @param pageDto 페이징 정보
     * @return Slice&lt;ChannelResponseDto&gt;
     */
    @GetMapping("/channels")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<ChannelResponseDto> getAllChannels(@ModelAttribute PageDto pageDto) {
        String infoLog = "[GET] /api/admins/channels";
        log.info(infoLog);

        return channelService.getAllChannelsToPage(pageDto);
    }

    /**
     * 대상 채널 아래의 모든 쓰레드들을 조회
     *
     * @param channelId 대상 채널
     * @param pageDto   페이징 정보
     * @return SLice&lt;ThreadResponseDto&gt;
     */
    @GetMapping("/channels/{channelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Slice<ThreadResponseDto> getAllThreadsInChannel(@PathVariable Long channelId,
                                                           @ModelAttribute PageDto pageDto) {
        String infoLog = "[GET] /api/admins/channels/" + channelId + "/threads/";
        log.info(infoLog);

        return threadService.getAllThreadsInChannel(channelId, pageDto);
    }

    /**
     * 대상 쓰레드 아래 모든 코멘트들을 조회
     *
     * @param channelId 대상 채널
     * @param threadId  대상 쓰레드
     * @param pageDto   페이징 정보
     * @return SLice&lt;CommentResponseDto&gt;
     */
    @GetMapping("/channels/{channelId}/threads/{threadId}/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public Slice<CommentResponseDto> getAllCommentsInThread(
            @PathVariable Long channelId,
            @PathVariable Long threadId,
            @ModelAttribute PageDto pageDto) {

        String infoLog = "[GET] /api/admins/channels/" + channelId + "/threads/" + threadId + "/comments";
        log.info(infoLog);

        return commentService.getAllCommentsInThread(threadId, pageDto);
    }

    // Update //

    /**
     * 채널 정보 수정 ( 관리자 )
     * [PATCH] /api/admins/channels/{channelId}
     *
     * @param requestDto 초대할 사람의 이메일
     * @param channelId  초대할 채널 ID
     */
    @PatchMapping("/admins/channels/{channelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateChannelName(@PathVariable Long channelId,
                                  @RequestBody ChannelDto.ChannelRequest requestDto) {
        // channel name check
        if (requestDto.getChannelName().trim().equals("")) {
            throw new IllegalArgumentException("채널 이름에는 공백이 들어갈 수 없습니다.");
        }

        // update channel name
        channelService.updateChannelName(channelId, requestDto.getChannelName());
    }

    // Disable ( 논리 삭제 ) //

    /**
     * 어드민이 대상 채널과 그 하위 데이터들을 비활성화(논리 삭제)합니다.
     *
     * @param channelId 대상 채널
     */
    @PatchMapping("/channels/{channelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableChannel(@PathVariable Long channelId) {
        String infoLog = "[PATCH] /api/admins/channels/" + channelId;
        log.info(infoLog);

        userChannelService.disableChannel(channelId);
    }

    /**
     * 어드민이 대상 쓰레드와 그 하위 데이터들을 비활성화(논리 삭제)합니다.
     *
     * @param threadId 대상 쓰레드
     */
    @PatchMapping("/threads/{threadId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableThread(@PathVariable Long threadId) {
        String infoLog = "[PATCH] /api/admins/threads/" + threadId;
        log.info(infoLog);

        threadService.disableThread(threadId);
    }

    /**
     * 어드민이 대상 코멘트와 그 하위 데이터들을 비활성화(논리 삭제)합니다.
     *
     * @param commentId 대상 코멘트
     */
    @PatchMapping("/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableComment(@PathVariable Long commentId) {
        String infoLog = "[PATCH] /api/admins/comments/" + commentId;
        log.info(infoLog);

        commentService.disableCommentByAdmin(commentId);
    }

}
