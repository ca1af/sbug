package com.sparta.sbug.channel.controller;

import com.sparta.sbug.channel.dto.ChannelDto;
import com.sparta.sbug.channel.dto.ChannelRequestDto;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.upperlayerservice.UserChannelUpperLayerService;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class ChannelController {
    private final ChannelServiceImpl channelService;
    private final UserChannelUpperLayerService userChannelUpperLayerService;

    // 채널 정보 불러오기
    @GetMapping("/channels/{id}")
    public String channel(@PathVariable Long id) {
        Channel channel = channelService.getChannelById(id);
        return channel.getChannelName();
    }

    // 유저가 속한 채널의 리스트를 불러오기
    @GetMapping("/users/channels")
    public List<ChannelResponseDto> allMyChannel(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userChannelUpperLayerService.getChannelsByUserId(userDetails.getUser().getId());
    }

    // 채널 생성
    @PostMapping("/channels")
    public String channel(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ChannelRequestDto requestDto) {
        userChannelUpperLayerService.createChannelAndUserChannelForRequester(userDetails.getUser(), requestDto.getChannelName());
        return "home";
        // 특정 쓰레드로 가고 싶을 때는... 쓰레드 ID 같은 걸 줘서 프론트가 조회하게.. 프론트가 어떻게 받게 할 것인지 고민해야 함
        // 프론트와 서버를 분리해야함! (API 서버와 웹 서버와 프론트의 관점)
    }

    // 채널에 유저 초대하기
    @PostMapping("/channels/{channelId}/users")
    public String inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                             @RequestParam("email") String email,
                             @PathVariable Long channelId) {
        userChannelUpperLayerService.inviteUser(userDetails.getUser(), channelId, email);
        return "Success";
    }

    // 채널 정보 수정
    @PatchMapping("/channels/{channelId}")
    public String channelName(@PathVariable Long channelId,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @RequestBody ChannelDto.ChannelRequest requestDto) {
        channelService.updateChannelName(channelId, userDetails.getUser(), requestDto.getChannelName());
        return "redirect home";
    }

    // 채널 삭제
    @DeleteMapping("/channels/{channelId}")
    public String channel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @PathVariable Long channelId) {
        userChannelUpperLayerService.deleteChannelAndDependentUserChannel(userDetails.getUser(), channelId);
        return "redirect home";
    }

//    @GetMapping("/channel/{id}/threads")
//    public List<ThreadResponseDto> getThreads(@PathVariable Long id){
//        return channelService.getThreads(id);
//    }
}
