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
    private static final String code = "https://kauth.kakao.com/oauth/authorize?client_id=a6be9b62b761e5b5ee34bfa49d268617&redirect_uri=http://localhost:8080/api/users/kakao&response_type=code";

    // code 는 아래와 같습니다.
//    https://kauth.kakao.com/oauth/authorize?client_id=a6be9b62b761e5b5ee34bfa49d268617&redirect_uri=http://localhost:8080/api/users/kakao&response_type=code

    @GetMapping("api/users/kakao")
    public TokenResponse kakaoLogin() throws JsonProcessingException {
        return kakaoService.kakaoLogin(code);
    }
}
