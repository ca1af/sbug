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
    // 리턴값은 성공/실패 정보를 줄 수 있는. HTTP통신답게...
    // ResponseEntity<> 사용 권장 (200 / 400 등) / -> 간단한 건 status 만 줘도 될 듯.
    // Json 타입의 데이터 필요하면 Body 에 << DTo 형식으로 주면 될 듯.
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
