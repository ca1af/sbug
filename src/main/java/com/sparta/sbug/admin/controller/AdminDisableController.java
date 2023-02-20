package com.sparta.sbug.admin.controller;

import com.sparta.sbug.admin.dto.AdminResponseDto;
import com.sparta.sbug.admin.service.AdminService;
import com.sparta.sbug.security.dto.TokenResponseDto;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.admin.service.AdminUpperLayerService;
import com.sparta.sbug.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminDisableController {
    private final AdminService adminService;
    private final JwtProvider jwtProvider;
    private final AdminUpperLayerService adminUpperLayerService;

    @PostMapping("/login")
    public TokenResponseDto adminLogin(@RequestBody LoginRequestDto requestDto){
        AdminResponseDto responseDto = adminService.adminLogin(requestDto);
        return jwtProvider.createTokenAdmin(responseDto.getEmail());
    }

    @PatchMapping("/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableComment(@PathVariable Long commentId){
        adminUpperLayerService.disableComment(commentId);
    }

    @PatchMapping("/threads/{threadId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableThread(@PathVariable Long threadId){
        adminUpperLayerService.disableThread(threadId);
    }
    @PatchMapping("/channels/{channelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableChannel(@PathVariable Long channelId){
        adminUpperLayerService.disableChannelAndDependentUserChannel(channelId);
    }
}
