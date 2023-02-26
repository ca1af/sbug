package com.sparta.sbug.channel.controller;

import com.sparta.sbug.channel.dto.ChannelDto;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.upperlayerservice.UserChannelUpperLayerService;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework web bind
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChannelController {
    private final ChannelServiceImpl channelService;

    /**
     * 하위 레이어 데이터 서비스 - 유저-채널 서비스
     */
    private final UserChannelUpperLayerService userChannelUpperLayerService;


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
        log.info("[POST] /api/channels");

        // channel name check
        if (requestDto.getChannelName().trim().equals("")) {
            throw new IllegalArgumentException("채널 이름에는 공백이 들어갈 수 없습니다.");
        }

        // create channel and user-channel mapping data
        return userChannelUpperLayerService.createChannelAndUserChannelForRequester(userDetails.getUser(), requestDto.getChannelName());
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
        log.info("[GET] /api/users/channels");

        return userChannelUpperLayerService.getChannelsByUserId(userDetails.getUser().getId());
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
        String infoLog = "[POST] /api/channels/" + channelId + "/users";
        log.info(infoLog);

        userChannelUpperLayerService.inviteUser(userDetails.getUser(), channelId, requestDto.getEmail());
    }
}
