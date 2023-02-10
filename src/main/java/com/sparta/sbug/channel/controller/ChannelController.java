package com.sparta.sbug.channel.controller;

import com.sparta.sbug.channel.dto.ChannelDto;
import com.sparta.sbug.channel.dto.ChannelRequestDto;
import com.sparta.sbug.channel.dto.ChannelResponseDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.UserChannelUpperLayerService;
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

    @GetMapping("/channel/{id}")
    public String getChannel(@PathVariable Long id) {
        Channel channel = channelService.getChannelById(id);
        return channel.getChannelName();
    }

    @GetMapping("/user/channels")
    public List<ChannelResponseDto> getAllMyChannel(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userChannelUpperLayerService.getChannelsByUserId(userDetails.getUser().getId());
    }

    @PostMapping("/channel/create")
    public String createChannel(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ChannelRequestDto requestDto) {
        userChannelUpperLayerService.createChannelAndUserChannelForRequester(userDetails.getUser(), requestDto.getChannelName());
        return "home";
    }

    @PostMapping("/channel/{channelId}/invite")
    public String inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                             @RequestBody ChannelDto.InvitationRequest requestDto,
                             @PathVariable Long channelId) {

        userChannelUpperLayerService.inviteUser(userDetails.getUser(), channelId, requestDto.getEmail());
        return "Success";
    }

    @PatchMapping("/channels/{channelId}/update")
    public String updateChannelName(@PathVariable Long channelId,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @RequestBody ChannelDto.ChannelRequest requestDto) {
        channelService.updateChannelName(channelId, userDetails.getUser(), requestDto.getChannelName());
        return "redirect home";
    }

    @DeleteMapping("/channels/{channelId}/delete")
    public String deleteChannel(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @PathVariable Long channelId) {
        userChannelUpperLayerService.deleteChannelAndDependentUserChannel(userDetails.getUser(), channelId);
        return "redirect home";
    }

//    @GetMapping("/channel/{id}/threads")
//    public List<ThreadResponseDto> getThreads(@PathVariable Long id){
//        return channelService.getThreads(id);
//    }
}
