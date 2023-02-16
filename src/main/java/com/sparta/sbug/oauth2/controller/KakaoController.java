package com.sparta.sbug.oauth2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.sbug.oauth2.service.KakaoService;
import com.sparta.sbug.security.dto.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;

    //https://kauth.kakao.com/oauth/authorize?client_id=a6be9b62b761e5b5ee34bfa49d268617&redirect_uri=http://localhost:5500/login.html&response_type=code
    @GetMapping("/api/users/kakao")
    public TokenResponseDto kakaoLogin(HttpServletRequest request) throws JsonProcessingException {
        var code = request.getHeader("code");
        return kakaoService.kakaoLogin(code);
    }
}
