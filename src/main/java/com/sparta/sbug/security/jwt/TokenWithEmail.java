package com.sparta.sbug.security.jwt;

import lombok.Getter;

@Getter
public class TokenWithEmail {
    private final String email;

    private final String type;

    private TokenWithEmail(String email, String type) {
        this.email = email;
        this.type = type;
    }

    public static TokenWithEmail atk(String email) {
        return new TokenWithEmail(email, "ATK");
    }

    public static TokenWithEmail rtk(String email) {
        return new TokenWithEmail(email, "RTK");
    }
}
