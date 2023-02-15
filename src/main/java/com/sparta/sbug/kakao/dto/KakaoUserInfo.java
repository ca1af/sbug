package com.sparta.sbug.kakao.dto;

import lombok.Getter;

@Getter
public class KakaoUserInfo {
    private Long id;
    private String email;
    private String nickname;

    public KakaoUserInfo(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
}


