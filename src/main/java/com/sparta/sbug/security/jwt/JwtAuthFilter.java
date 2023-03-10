package com.sparta.sbug.security.jwt;

import com.sparta.sbug.security.userDetails.AdminDetailsServiceImpl;
import com.sparta.sbug.security.userDetails.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

// lombok
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsImpl;
    private final AdminDetailsServiceImpl adminDetailsService;

    /**
     * JWT 권한 필터
     * Access Token, Refresh Token의 검증을 수행합니다.
     *
     * @param request     Http 서블릿 요청
     * @param response    Http 서블릿 응답
     * @param filterChain 필터 체인
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null) {
            log.info("AccessToken in JwtAuthFilter = " + accessToken);
            String atk = accessToken.substring(7);
            String rtk = "";
            if (!this.validateToken(atk)) {
                String refreshToken = request.getHeader("RTK");
                log.info("RefreshToken in JwtAuthFilter = " + refreshToken);
                if (refreshToken != null) {
                    rtk = refreshToken.substring(7);
                }

                if (!validateToken(rtk)) {
                    response.sendError(403, "권한 없음. 다시 로그인 해주세요");
                } else if (!request.getRequestURI().equals("/account/reissue")) {
                    response.sendError(401, "만료되었습니다. reissue");
                }
            }

            if (!request.getRequestURI().equals("/account/reissue")) {
                setAuthentication(request, atk);
            } else {
                setAuthentication(request, rtk);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 토큰에서 정보를 꺼내 UserDetails를 만들고 Security Context Holder에 Authentication을 담기
     *
     * @param request Http 서블릿 요청
     * @param token   토큰 (atk 또는 rtk)
     */
    private void setAuthentication(HttpServletRequest request, String token) {
        try {
            String email = jwtProvider.getSubject(token);
            if (email.contains("@")){
                UserDetails userDetails = userDetailsImpl.loadUserByUsername(email);
                Authentication authentication = jwtProvider.createAuthentication(userDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                UserDetails userDetails = adminDetailsService.loadUserByUsername(email);
                Authentication authentication = jwtProvider.createAuthentication(userDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException e) {
            request.setAttribute("exception", e.getMessage());
        }
    }

    /**
     * 토큰을 검증하는 메서드
     *
     * @param token 검증할 토큰
     * @return boolean : true = 검증 성공, false = 검증 실패
     */
    public boolean validateToken(String token) {
        try {
            var encodeKey = Base64.getEncoder().encodeToString(jwtProvider.byteKey);
            Jwts.parserBuilder().setSigningKey(encodeKey).build().parseClaimsJws(token);
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

}
