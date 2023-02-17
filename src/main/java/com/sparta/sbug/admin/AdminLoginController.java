package com.sparta.sbug.admin;

import com.sparta.sbug.admin.dto.AdminResponseDto;
import com.sparta.sbug.admin.service.AdminService;
import com.sparta.sbug.security.dto.TokenResponseDto;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.upperlayerservice.AdminUpperLayerService;
import com.sparta.sbug.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminLoginController {
    private final AdminService adminService;
    private final JwtProvider jwtProvider;
    private final AdminUpperLayerService adminUpperLayerService;

    @PostMapping("/login")
    public TokenResponseDto adminLogin(@RequestBody LoginRequestDto requestDto){
        AdminResponseDto responseDto = adminService.adminLogin(requestDto);
        return jwtProvider.createTokenAdmin(responseDto.getEmail());
    }

    @PatchMapping("/comments/{commentId}")
    public void disableComment(@PathVariable Long commentId){
        adminUpperLayerService.disableComment(commentId);
    }

    @PatchMapping("/threads/{threadId}")
    public void disableThread(@PathVariable Long threadId){
        adminUpperLayerService.disableThread(threadId);
    }
    @PatchMapping("/channels/{channelId}")
    public void disableChannel(@PathVariable Long channelId){
        adminUpperLayerService.disableChannelAndDependentUserChannel(channelId);
    }
}
