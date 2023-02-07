package com.sparta.sbug.user.controller;

import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.dto.LoginRequestDto;
import com.sparta.sbug.user.dto.SignUpRequestDto;
import com.sparta.sbug.user.dto.UserUpdateDto;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/api/user/sign-up")
    public String signup(@RequestBody SignUpRequestDto requestDto){
        return userService.signup(requestDto);
    }
    @PostMapping("/api/user/login")
    public String login(@RequestBody LoginRequestDto requestDto){
        return userService.login(requestDto);
    }
    @DeleteMapping("/api/user/unregister")
    public String unregister(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.unregister(userDetails.getUser());
    }
    @PutMapping("/api/user/update")
    public String update(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UserUpdateDto dto){
        userService.update(userDetails.getUser(),dto);
        return "updated";
    }
    @GetMapping("/api/users")
    public List<User> getUsers(){
        return userService.getUsers();
    }
}
