package com.sparta.sbug.channel.controller;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.CascadeService;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChannelController {
    private final ChannelServiceImpl channelService;
    private final CascadeService cascadeService;
    @GetMapping("/channel/{id}")
    public String getChannel(@PathVariable("id") Long id){
        Channel channel = channelService.getChannel(id);
        return channel.getChannelName();
    }

    @PostMapping("/channel/create")
    public String createChannel(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(name = "channel-name") String channelName){
        channelService.createChannel(userDetails.getUser(), channelName);
        return "home";
    }
    // requestParam < Get에서 주로 사용한다.

    @PostMapping("/channel/{id}/invite")
    public String inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(name = "email") String email, @PathVariable Long id){
        return channelService.inviteUser(userDetails.getUser(), id, email);
    }

    @PatchMapping("/channel/update/{id}")
    public String updateChannelName(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(name = "channel-name") String channelName){
        channelService.updateChannelName(id,userDetails.getUser(),channelName);
        return "redirect home";
    }
    @DeleteMapping("/channel/delete/{id}")
    public String deleteChannel(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        cascadeService.cascadeDelete(userDetails.getUser(), id);
        return "redirect home";
    }
    @GetMapping("/channel/{id}/threads")
    public List<ThreadResponseDto> getThreads(@PathVariable Long id){
        return channelService.getThreads(id);
    }
}
