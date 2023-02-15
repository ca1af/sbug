package com.sparta.sbug.kakao.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.sbug.kakao.service.KakaoService;
import com.sparta.sbug.security.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService kakaoService;

    //https://kauth.kakao.com/oauth/authorize?client_id=36615bca254358f5c0260a0485d71aac&redirect_uri=http://localhost:8080/api/user/kakao&response_type=code
    @GetMapping("/api/users/kakao")
    public TokenResponse kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        return kakaoService.kakaoLogin(code);
    }

}
