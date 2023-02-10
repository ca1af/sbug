package com.sparta.sbug.security.jwt;

import com.sparta.sbug.security.dto.JwtDto;
import com.sparta.sbug.security.userDetails.UserDetailsServiceImpl;
import com.sparta.sbug.user.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 14 * 24 * 60 * 60 * 1000;
    // 7~30 일 사이로 설정한다고 합니다. 14일로 설정했습니다.

    private final UserDetailsServiceImpl userDetailsService;
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct// 의존성 주입이 이루어진 후 초기화를 수행하는 메소드
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);// Base64로 인코딩 되어있는 문자열을 byte 배열로 변환
        //* Base64 (64진법) : 바이너리(2진) 데이터를 문자 코드에 영향을 받지 않는 공통 ASCII문자로 표현하기 위해 만들어진 인코딩
        key = Keys.hmacShaKeyFor(bytes); // 바이트 배열을 HMAC-SHA 알고리즘을 사용해 Key객체로 반환, 이를 key변수에 대입
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        // Http프로토콜의 request정보를 서블릿에게 전달하기 위한 목적으로 사용하는 매개변수
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // 우리가 아래에서 설정한 "Authorization"의 헤더를 가져옴
        // StringUtils.hasText는 값이 있을경우 true , 공백이거나 Null이 들어온 경우 false반환
        // bearerToken.startsWith는 필드값이 해당 변수와 동일한지 확인
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
            // bearer 제외하고 나머지 문자열 반환
            //substring(int beginIndex)
            //substring(int beginIndex, int endIndex)
            /*
            인자로 beginIndex만 전달하면, 이 index가 포함된 문자부터 마지막까지 잘라서 리턴합니다.
            위의 내용은 7번째 부터 끝까지 문자열을 리턴한다는 의미이다.
            여기서는 BEARER_PREFIX 에 들어있는 "Bearer " 부분을 잘라내고 보내주기 위해서 넣은것이다.
            인자로 beginIndex, endIndex를 모두 전달하면 begin을 포함한 문자부터
            endIndex 이전 index의 문자까지 잘라서 리턴합니다.
             */
        }
        return null;
    }

    // 토큰 생성
    public JwtDto createToken(String username, UserRole role) {
        Date date = new Date();

        String accessToken = BEARER_PREFIX + Jwts.builder()
                .setSubject(username)// 토큰 용도
                .claim(AUTHORIZATION_KEY, role)// payload에 들어갈 정보 조각들
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))// 만료시간 설정
                .setIssuedAt(date)// 토큰 발행일
                .signWith(key, signatureAlgorithm) // key변수 값과 해당 알고리즘으로 sign
                .compact();// 토큰 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(BEARER_PREFIX)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRATION_TIME)
                .build();
    }

    // 토큰 검증
    // Header에서 토큰 가져오기
    public boolean validateToken(String token, HttpServletResponse response) {
        //parser : parsing을 하는 도구. parsing : token에 내재된 자료 구조를 빌드하고 문법을 검사한다.
        // JwtParseBuilder인스턴스를 생성
        // 서명 검증을 위한 키를 지정 setSigningKey()
        // 스레드에 안전한 JwtPaser를 리턴하기 위해 JwtPaserBuilder의 build()메서드를 호출
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {// 전: 권한 없다면 발생 , 후: JWT가 올바르게 구성되지 않았다면 발생
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");

        } catch (ExpiredJwtException e) {// JWT만료
            log.info("Expired JWT token, 만료된 JWT token 입니다.");

        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");

        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");

        }
        return false;
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email) {
        //
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        //userDetailsServiceImpl에서 loadUserByUsername메소드를 호출하여
        //DB에서 username과 같은 사용자를 찾은후 UserDetailsImpl(UserDetails를 인터페이스를 상속한)형태로
        //반환하여 다형성을 만족시키는 형태로 UserDetails 로 받는다.
        //UserDetailsImpl 인터페이스 안에는 유저라는 객체와 유저이름 이렇게 2개의 변수가있고
        //아래에 getAuthorities()라는 메소드가 있는데 자세한 설명은 바로 아래에서 하겠다.
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //스프링 UsernamePasswordAuthenticationToken
        //에대해서 검색하면 자세한 설명을 알수있고 간단한 설명으로는
        /*
                인터페이스 Principal에서 상속받은 인터페이스 Athentication
                추상클래스로서 Athentication 를 넣은 AbstratAuthenticationToken
                그리고 이 내용을 구현한 구현클래스가UsernamePasswordAuthenticationToken입니다.
                Principal -> Athentication -> AbstratAuthenticationToken -> UsernamePasswordAuthenticationToken
                다형성으로 한 메소드 안에서 구현하면서 userId를 넣어줄경우
                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                                            userId, null, userDetails.getAuthorities());
                이러한 형식으로 넣어서 구현해주면 됩니다.
                Principal의 경우 타입정보(getClass)와 이름(getName)만 가지고 있습니다.
         */
        /*
            Authentication 객체
            UsernamePasswordAuthenticationToken 구현체 및 세션 정보를
            보관하는 객체에서 필요한 정보를 뽑아내는 메소드를 가지고 있습니다
            게다가 아래와 같은 방법들이있고 저희는 그중 3번째인 세 번째 생성자인 권한 리스트 객체 반환 을 반환을 사용하여
            UsernamePasswordAuthenticationToken의 마지막에 넣어주는것과 userDetails에 유저에 대한 객체도 넣어줍니다.
            Object getPrincipal() : 첫 번째 생성자로 주입한 객체 반환
            Object getCredentials() : 두 번째 생성자로 주입한 객체 반환
            Collection<? extends GrantedAuthority> getAuthorities() : 세 번째 생성자인 권한 리스트 객체 반환
            Object getDetails() : 세션정보를 가진 WebAuthenticationDetails 객체 반환
            이것으로 sercurity패키지 안에있는 두 클래스의 자세하면서도 간단한 설명은 맞추었고
            이게 config로 넘어가도록 하겠다.
         */
    }
    // 토큰에서 사용자 정보 가져오기
    // 위의 검증식과 일치하나 마지막에 getBody 를 통해서 안에 들어있는 값을 가져온다.
    // 앞의 validateToken 부분에서 이미 검증을 거쳤기 때문에 따로 검증을 거치지 않고 바로 넣어준다.
    // 등록된 클레임 이름,
    // 공용 클레임 이름 및 개인 클레임 이름.
    // Claims 정보의 한 덩어리를 클레임(claim)이라고 부르며, 클레임은 key-value의 한 쌍으로 이루어져있습니다
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}