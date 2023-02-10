package com.sparta.sbug.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sbug.security.dto.SecurityExceptionDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.sparta.sbug.security.jwt.JwtUtil.REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String accessToken = jwtUtil.resolveAccessToken(request);
//        String refreshToken = jwtUtil.resolveRefreshToken(request);
//        if(accessToken != null) {
//            //JwtUtil 클래스 메소드인 vaildateToken에서 토큰을 검사한다.
//            //토큰에 문제가 있을때 if문을 실행시킨다.
//            if(!jwtUtil.validateToken(accessToken, response)){
////                throw new SecurityException("토큰이 유효하지 않습니다");
//                jwtExceptionHandler(response, "Invalid JWT signature", HttpStatus.BAD_REQUEST.value());
//                return;
//            }
//            // 정보의 한 덩어리를 클레임(claim)이라고 부르며, 클레임은 key-value의 한 쌍으로 이루어져있습니다
//            // jwtUtil의 getUserInfoFromToken 메소드를 통하여 claims 형태로 claims info변수에 token정보를 받습니다.
//            Claims info = jwtUtil.getUserInfoFromToken(accessToken);
//            //이제 아래 setAuthentication으로 받은 claims를 전송하여
//            //Authentication 설정을해준다.
//            //새로운 컨텍스트를 생성하여 누가 인증했는지에대한 정보를 저장하는 메소드이다.
//            setAuthentication(info.getSubject());
//        }
//        //filterChain은 체인의 다음 필터를 호출하거나 호출 필터가 체인의 마지막 필터인 경우 체인 끝에 리소스를 호출합니다.
//        filterChain.doFilter(request,response);
//    }
@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String accessToken = jwtUtil.resolveAccessToken(request);
    String refreshToken = request.getHeader(REFRESH_TOKEN); // BEARER 없이 가져온 순수 토큰 값
    String redisToken = redisTemplate.opsForValue().get("RefreshToken");

    if(accessToken != null) {
        //JwtUtil 클래스 메소드인 vaildateToken에서 토큰을 검사한다.
        //토큰에 문제가 있을때 if문을 실행시킨다.
        if(jwtUtil.validateToken(accessToken, response)){
            Claims info = jwtUtil.getUserInfoFromToken(accessToken);
            setAuthentication(info.getSubject());
        } else if(!jwtUtil.validateToken(accessToken, response) && !refreshToken.equals(redisToken)){
            // 토큰이 언밸리드하고,

            // 리프레쉬 토큰 이용한 액세스 토큰 재발급 들어가야 함.
            // redis 에는 보통 만료된 리프레쉬 토큰 저장한다. < 만약 조회가 된다? 그럼 만료된거라서
            // 액세스 토큰을 재발급 하면 안됨. (다시 로그인 해야함.)
            // TTL -> 해당 레코드를 삭제 할 시기 time to live = 60*60~

            // 로그아웃 하면 리프레쉬 토큰 만료 -> 기간은 남아있는.
            // 그래서 이 친구는 이미 로그아웃 했으니...액세스 토큰 새로 발급 X, 리프레쉬 토큰부터 새로 발급 절차 밟기.

            // 로그아웃 할 때 레디스에 저장(리프레쉬 토큰)
            // 여기는 로그인에서 사용하는 filter 니까 레디스에서 refreshToken 대조해보고 같으면 새로 발급(토큰둘다).


        }
    }
    //filterChain은 체인의 다음 필터를 호출하거나 호출 필터가 체인의 마지막 필터인 경우 체인 끝에 리소스를 호출합니다.
    filterChain.doFilter(request,response);
}

    //SecurityContextHolder에 관한 사이트
    // https://00hongjun.github.io/spring-security/securitycontextholder/
    //
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        //jwtUtil에서  username에 알맞은 User객체를 가져와서 Authentication에 넣어준다.
        Authentication authentication = jwtUtil.createAuthentication(email);
        //그리고 빈 컨텍스트에 가져온 데이터(User객체와, username) authentication변수를 넣어주고
        context.setAuthentication(authentication);
        //누가 인증하였는지에 대한 정보들을 저장한다.
        SecurityContextHolder.setContext(context);
    }

        /* 스프링 시큐리티의 Authentication
            Authentication - SecurityContext의 user, 인증 정보를 가지고 있으며, AuthenticationManager에 의해 제공됩니다.
            GrantedAuthority - 인증 주체에게 부여된 권한 (roles, scopes, etc.)
            AuthenticationManager - Spring Security의 필터에서 인증을 수행하는 방법을 정의하는 API입니다.
            ProviderManager - AuthenticationManager의 구현체
            AuthenticationProvider - 인증 수행을 위해 ProviderManager에 의해 사용 됩니다.
            AbstractAuthenticationProcessingFilter - 인증에 사용되는 기본 Filter입니다. 인증의 흐름이 어떻게 이루어 지는지 잘 보여줍니다.

            SecurityContextHolder - 누가 인증했는지에 대한 정보들을 저장하고 있습니다.
            SecurityContext - 현재 인증한 user에 대한 정보를 가지고 있습니다
         */
    //SecurityContextHolder.createEmptyContext() 구성된 전략에 대해 빈 컨텍스트를 새로 생성하도록 위임합니다.

    //예외가 발생했을때 사용되는 메소드
    //맨위에 doFilterInternal 메소드에서 토큰이 틀렸을때
    //사용된다.
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}