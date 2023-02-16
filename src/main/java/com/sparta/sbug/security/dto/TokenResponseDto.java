package com.sparta.sbug.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰 응답을 위한 DTO
 */
// lombok
@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private final String atk;
    private final String rtk;
}
