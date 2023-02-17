package com.sparta.sbug.security.jwt;

import com.sparta.sbug.common.exceptions.ErrorCode;
import com.sparta.sbug.security.RedisDao;
import com.sparta.sbug.security.dto.TokenResponseDto;
import com.sparta.sbug.common.exceptions.CustomException;
import com.sparta.sbug.user.dto.UserResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework stereotype
@Component
public class JwtProvider {

    private final RedisDao redisDao;

    public static final String Bearer = "Bearer ";

    @Value("${jwt.secret.key}")
    protected byte[] byteKey;

    /**
     * byteKey가 HMAC-SHA 알고리즘으로 암호화 된 키
     */
    protected Key key;

    @Value("${jwt.live.atk}")
    private Long atkTime;

    @Value("${jwt.live.rtk}")
    private Long rtkTime;

    /**
     * JwtProvider Bean 주입 직후, byteKey를 HMAC-SHA 알고리즘으로 암호화하여 저장합니다.
     */
    @PostConstruct
    protected void init() {
        key = Keys.hmacShaKeyFor(this.byteKey);
    }

    public Authentication createAuthentication(UserDetails userDetails){
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    /**
     * Access Token 재발행(reissue)
     *
     * @param userResponseDto 재발행 유저 정보
     * @return TokenResponseDto : 토큰을 담아 반환할 DTO
     */
    public TokenResponseDto reissueAtk(UserResponseDto userResponseDto) {
        String email = userResponseDto.getEmail();
        String rtkInRedis = redisDao.getValues(email);
        if (Objects.isNull(rtkInRedis)) throw new CustomException(ErrorCode.CREDENTIAL_EXPIRATION);
        String atk = createToken(email, atkTime);
        String rtk = createToken(email, rtkTime);
        redisDao.setValues(email, rtk, Duration.ofMillis(rtkTime));
        return new TokenResponseDto(atk, rtk);
    }

    /**
     * 유저 로그인 후 토큰 발행
     *
     * @param userResponseDto 발행 유저 정보
     * @return TokenResponseDto : 토큰을 담아 반환할 DTO
     */
    public TokenResponseDto createTokensByLogin(UserResponseDto userResponseDto) {
        String email = userResponseDto.getEmail();

        String atk = createToken(email, atkTime);
        String rtk = createToken(email, rtkTime);
        redisDao.setValues(email, rtk, Duration.ofMillis(rtkTime));
        return new TokenResponseDto(atk, rtk);
    }
    public TokenResponseDto createTokenKakao(String email) {
        String atk = createToken(email, atkTime);
        String rtk = createToken(email, rtkTime);
        redisDao.setValues(email, rtk, Duration.ofMillis(rtkTime));
        return new TokenResponseDto(atk, rtk);
    }

    public TokenResponseDto createTokenAdmin(String email) {
        String atk = createToken(email, atkTime);
        String rtk = createToken(email, rtkTime);
        redisDao.setValues("ADMIN" + email, rtk, Duration.ofMillis(rtkTime));
        return new TokenResponseDto(atk, rtk);
    }


    /**
     * 토큰을 만드는 메서드
     *
     * @param email     토큰에 포함될 발행 유저 이메일
     * @param tokenLive 토큰의 수명
     * @return String
     */
    private String createToken(String email, Long tokenLive) {
        //String subjectStr = objectMapper.writeValueAsString(email);
        Claims claims = Jwts.claims()
                .setSubject(email);
        Date date = new Date();
        return Bearer + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenLive))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에 포함된 발행 유저의 이메일을 반환하는 메서드
     *
     * @param token 토큰
     * @return String
     */
    public String getSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 레디스에서 Refresh Token 을 지우는 메서드
     *
     * @param key Refresh Token
     */
    public void deleteRtk(String key) {
        redisDao.deleteValues(key);
    }
}