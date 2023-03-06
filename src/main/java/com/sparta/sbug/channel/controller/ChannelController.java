package com.sparta.sbug.channel.controller;

import com.sparta.sbug.channel.dto.ChannelDto;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.userchannel.service.UserChannelService;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.sbug.common.exceptions.ErrorCode.BAD_REQUEST_CHANNEL_NAME;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChannelController {

    /**
     * 하위 레이어 데이터 서비스 - 유저-채널 서비스
     */
    private final UserChannelService userChannelService;


    /**
     * 채널 만들기
     * [POST] /api/channels
     *
     * @param userDetails 요청자 정보
     * @param requestDto  요청 정보(채널 이름)
     * @return ChannelResponseDto
     */
    @PostMapping("/channels")
    public ChannelResponseDto createChannel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody ChannelDto.ChannelRequest requestDto) {
        // channel name check
        if (requestDto.getChannelName().trim().equals("")) {
            throw new CustomException(BAD_REQUEST_CHANNEL_NAME);
        }

        // create channel and user-channel mapping data
        return userChannelService.createChannel(userDetails.getUser(), requestDto.getChannelName());
    }

    /**
     * 내가 속한 채널의 리스트를 불러오기
     * [GET] /api/users/channels
     *
     * @param userDetails 요청자 정보
     * @return List&lt;ChannelResponseDto&gt;
     */
    @GetMapping("/users/channels")
    public List<ChannelResponseDto> getAllMyChannel(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userChannelService.getChannelsByUserId(userDetails.getUser().getId());
    }

    /**
     * 채널에 유저 초대하기
     * [POST] /api/channels/{channelId}/users
     *
     * @param userDetails 요청자
     * @param requestDto  초대할 사람의 이메일
     * @param channelId   초대할 채널 ID
     */
    @PostMapping("/channels/{channelId}/users")
    public void inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestBody ChannelDto.InvitationRequest requestDto,
                           @PathVariable Long channelId) {
        userChannelService.inviteUser(userDetails.getUser(), channelId, requestDto.getEmail());
    }
}
