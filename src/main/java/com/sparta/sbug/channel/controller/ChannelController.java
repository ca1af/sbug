package com.sparta.sbug.channel.controller;

import com.sparta.sbug.channel.dto.ChannelRequestDto;
import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChannelController {
    private final ChannelServiceImpl channelService;
    @GetMapping("/channel/{channelId}")
    public Channel getChannel(@PathVariable("channelId") Long channelId){
        return channelService.getChannel(channelId);
    }

    @PostMapping("/channel/create")
    public String createChannel(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(name = "channel-name") String channelName){
        return channelService.createChannel(userDetails.getUser(), channelName);
    }

    @PostMapping("/channel/invite")
    public String inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(name = "email") String email, Channel channel){
        return channelService.inviteUser(userDetails.getUser(), channel, email);
    }

    @PatchMapping("/channel/update")
    public String updateChannelName(Channel channel, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam String channelName){
        channelService.updateChannelName(channel,userDetails.getUser(),channelName);
        return "redirect home";
    }
    @DeleteMapping("/channel/delete/{id}")
    public String deleteChannel(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        channelService.deleteChannel(userDetails.getUser(), id);
        return "redirect home";
    }

    @GetMapping("/channel/{id}/threads")
    public List<ThreadResponseDto> getThreads(@PathVariable Long id){
        return channelService.getThreads(id);
    }
}
