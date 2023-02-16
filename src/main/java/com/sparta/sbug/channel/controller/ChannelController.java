package com.sparta.sbug.channel.controller;

import com.sparta.sbug.channel.dto.ChannelDto;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.upperlayerservice.UserChannelUpperLayerService;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework web bind
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ChannelController {
    private final ChannelServiceImpl channelService;
    private final UserChannelUpperLayerService userChannelUpperLayerService;


    /**
     * 단일 채널 정보 불러오기
     * [GET] /api/channels/{id}
     *
     * @param id 정보를 불러올 채널의 ID
     * @return ChannelResponseDto
     */
    @GetMapping("/channels/{id}")
    public ChannelResponseDto channel(@PathVariable Long id) {
        Channel channel = channelService.getChannelById(id);
        return ChannelResponseDto.of(channel);
    }

    /**
     * 내가 속한 채널의 리스트를 불러오기
     * [GET] /api/users/channels
     *
     * @param userDetails 요청자 정보
     * @return List&lt;ChannelResponseDto&gt;
     */
    @GetMapping("/users/channels")
    public List<ChannelResponseDto> allMyChannel(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userChannelUpperLayerService.getChannelsByUserId(userDetails.getUser().getId());
    }

    // 채널 생성

    /**
     * 채널 만들기
     * [POST] /api/channels
     *
     * @param userDetails 요청자 정보
     * @param requestDto  요청 정보(채널 이름)
     */
    @PostMapping("/channels")
    public void channel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                        @RequestBody ChannelDto.ChannelRequest requestDto) {

        // channel name check
        if (requestDto.getChannelName().trim().equals("")) {
            throw new IllegalArgumentException("채널 이름에는 공백이 들어갈 수 없습니다.");
        }

        // create channel and user-channel mapping data
        userChannelUpperLayerService.createChannelAndUserChannelForRequester(userDetails.getUser(), requestDto.getChannelName());
    }


    /**
     * 채널에 유저 초대하기
     * [POST] /api/channels/{channelId}/users
     *
     * @param userDetails 요청자
     * @param requestDto  초대할 사람의 이메일
     * @param id          초대할 채널 ID
     */
    @PostMapping("/channels/{id}/users")
    public void inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestBody ChannelDto.InvitationRequest requestDto,
                           @PathVariable Long id) {
        // invite user (=create user-channel mapping data)
        userChannelUpperLayerService.inviteUser(userDetails.getUser(), id, requestDto.getEmail());
    }

    /**
     * 채널 정보 수정
     * [PATCH] /api/channels/{id}
     *
     * @param userDetails 요청자
     * @param requestDto  초대할 사람의 이메일
     * @param id          초대할 채널 ID
     */
    @PatchMapping("/channels/{id}")
    public void channelName(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails,
                            @RequestBody ChannelDto.ChannelRequest requestDto) {
        // channel name check
        if (requestDto.getChannelName().trim().equals("")) {
            throw new IllegalArgumentException("채널 이름에는 공백이 들어갈 수 없습니다.");
        }

        // update channel name
        channelService.updateChannelName(id, userDetails.getUser(), requestDto.getChannelName());
    }

    /**
     * 채널과 그 채널에 가입된 유저 데이터(유저-채널 데이터)를 삭제
     * [DELETE] /api/channels/{id}
     *
     * @param userDetails 유저 정보
     * @param id          채널 ID
     */
    @DeleteMapping("/channels/{id}")
    public void channel(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        // delete channel
        userChannelUpperLayerService.deleteChannelAndDependentUserChannel(userDetails.getUser(), id);
    }

//    @GetMapping("/channel/{id}/threads")
//    public List<ThreadResponseDto> getThreads(@PathVariable Long id){
//        return channelService.getThreads(id);
//    }
}
