package com.sparta.sbug.channel.controller;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.channel.service.CascadeService;
import com.sparta.sbug.channel.service.ChannelServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.thread.dto.ThreadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/channels")
    @ResponseStatus(HttpStatus.CREATED)
    public String createChannel(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(name = "channel-name") String channelName){
        channelService.createChannel(userDetails.getUser(), channelName);
        return "home";
        // 특정 쓰레드로 가고 싶을 때는... 쓰레드 ID 같은 걸 줘서 프론트가 조회하게.. 프론트가 어떻게 받게 할 것인지 고민해야 함
        // 프론트와 서버를 분리해야함! (API 서버와 웹 서버와 프론트의 관점)
    }

    @PostMapping("/channel/{id}/invite")
    public String inviteUser(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(name = "email") String email, @PathVariable Long id){
        return channelService.inviteUser(userDetails.getUser(), id, email);
    }

    @PatchMapping("/channels/{id}")
    public String updateChannelName(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(name = "channel-name") String channelName){
        channelService.updateChannelName(id,userDetails.getUser(),channelName);
        return "redirect home";
    }

    @DeleteMapping("/channel/{id}")
    public String deleteChannel(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        cascadeService.cascadeDelete(userDetails.getUser(), id);
        return "redirect home";
    }

    @GetMapping("/channels/{id}/threads")
    public List<ThreadResponseDto> getThreads(@PathVariable Long id){
        return channelService.getThreads(id);
    }
}
