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
public class ChannelController {
    private final ChannelServiceImpl channelService;
    private final UserChannelUpperLayerService userChannelUpperLayerService;

    @GetMapping("/channels/{id}")
    public String channel(@PathVariable Long id) {
        Channel channel = channelService.getChannelById(id);
        return channel.getChannelName();
    }

    @GetMapping("/user/channels")
    public List<ChannelResponseDto> allMyChannel(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userChannelUpperLayerService.getChannelsByUserId(userDetails.getUser().getId());
    }

    @PostMapping("/channels")
    public String channel(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ChannelRequestDto requestDto) {
        userChannelUpperLayerService.createChannelAndUserChannelForRequester(userDetails.getUser(), requestDto.getChannelName());
        return "home";
        // 특정 쓰레드로 가고 싶을 때는... 쓰레드 ID 같은 걸 줘서 프론트가 조회하게.. 프론트가 어떻게 받게 할 것인지 고민해야 함
        // 프론트와 서버를 분리해야함! (API 서버와 웹 서버와 프론트의 관점)
    }

    @PostMapping("/channels/{channelId}/invite")
    public String inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                             @RequestBody ChannelDto.InvitationRequest requestDto,
                             @PathVariable Long channelId) {

        userChannelUpperLayerService.inviteUser(userDetails.getUser(), channelId, requestDto.getEmail());
        return "Success";
    }

    @PatchMapping("/channels/{channelId}")
    public String channelName(@PathVariable Long channelId,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @RequestBody ChannelDto.ChannelRequest requestDto) {
        channelService.updateChannelName(channelId, userDetails.getUser(), requestDto.getChannelName());
        return "redirect home";
    }

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
