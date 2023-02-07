package com.sparta.sbug.user.controller;

import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    @RequestMapping("/api/user/sign-up")
    public String signup(@RequestBody SignUpRequestDto requestDto){
        return userService.signup(requestDto);
    }
    @RequestMapping("/api/user/login")
    public String login(@RequestBody LoginRequestDto requestDto){
        return userService.login(requestDto);
    }
    @RequestMapping("/api/user/unregister")
    public String unregister(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.unregister(userDetails.getUser());
    }
    @RequestMapping("/api/user/update")
    public String update(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserUpdateDto dto){
        userService.update(userDetails.getUser(),dto);
        return "updated";
    }
    @RequestMapping("/api/users")
    public List<User> getUsers(){
        return userService.getUsers();
    }
}
