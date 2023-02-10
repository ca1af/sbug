package com.sparta.sbug.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sbug.security.RedisDao;
import com.sparta.sbug.security.dto.TokenResponse;
import com.sparta.sbug.security.exception.ForbiddenException;
import com.sparta.sbug.user.dto.UserResponseDto;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final RedisDao redisDao;
    private final ObjectMapper objectMapper;
    public static final String Bearer = "Bearer ";

    @Value("${jwt.secret.key}")
    private String key;

    @Value("${jwt.live.atk}")
    private Long atkTime;

    @Value("${jwt.live.rtk}")
    private Long rtkTime;

    @PostConstruct
    protected void init() {
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    public TokenResponse reissueAtk(UserResponseDto userResponseDto) throws JsonProcessingException {
        String rtkInRedis = redisDao.getValues(userResponseDto.getEmail());
        if (Objects.isNull(rtkInRedis)) throw new ForbiddenException("인증 정보가 만료되었습니다.");
        TokenWithEmail atkTokenWithEmail = TokenWithEmail.atk(
                userResponseDto.getEmail());
        String atk = createToken(atkTokenWithEmail, atkTime);
        return new TokenResponse(atk, null);
    }

    public TokenResponse createTokensByLogin(UserResponseDto userResponseDto) throws JsonProcessingException {
        TokenWithEmail atkTokenWithEmail = TokenWithEmail.atk(
                userResponseDto.getEmail());

        TokenWithEmail rtkTokenWithEmail = TokenWithEmail.rtk(
                userResponseDto.getEmail());

        String atk = Bearer + createToken(atkTokenWithEmail, atkTime);
        String rtk = Bearer + createToken(rtkTokenWithEmail, rtkTime);
        redisDao.setValues(userResponseDto.getEmail(), rtk, Duration.ofMillis(rtkTime));
        return new TokenResponse(atk, rtk);
    }

    private String createToken(TokenWithEmail tokenWithEmail, Long tokenLive) throws JsonProcessingException {
        String subjectStr = objectMapper.writeValueAsString(tokenWithEmail);
        Claims claims = Jwts.claims()
                .setSubject(subjectStr);
        Date date = new Date();
        return Bearer + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenLive))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public TokenWithEmail getSubject(String atk) throws JsonProcessingException {
        String subjectStr = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(atk).getBody().getSubject();
        return objectMapper.readValue(subjectStr, TokenWithEmail.class);
    }
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}