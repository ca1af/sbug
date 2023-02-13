package com.sparta.sbug.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sbug.security.dto.SecurityExceptionDto;
import com.sparta.sbug.security.userDetails.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsImpl;
    @Value("${jwt.secret.key}")
    private String key = "7ZWt7ZW0OTntmZTsnbTtjIXtlZzqta3snYTrhIjrqLjshLjqs4TroZzrgpjslYTqsIDsnpDtm4zrpa3tlZzqsJzrsJzsnpDrpbzrp4zrk6TslrTqsIDsnpA=";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");

        if (accessToken != null) {
            var atk = accessToken.substring(7);
            if (!this.validateToken(atk)) {
                String refreshToken = request.getHeader("RTK");
                String rtk = "";
                if (refreshToken != null) {
                    rtk = refreshToken.substring(7);
                    jwtProvider.getSubject(rtk);
                }

                if (!validateToken(rtk)) {
                    response.sendError(403, "권한 없음. 다시 로그인 해주세요");
                } else if (!request.getRequestURI().equals("/account/reissue")) {
                    response.sendError(401, "만료되었습니다. reissue");
                }
            }


            // try 들어가기 전에 토큰 밸리데이션 로직 필요함.
            try {
                String email = jwtProvider.getSubject(atk);
                String requestURI = request.getRequestURI();
                if (email.equals("RTK") && !requestURI.equals("/account/reissue")) {
                    throw new JwtException("토큰을 확인하세요.");
                }
                UserDetails userDetails = userDetailsImpl.loadUserByUsername(email);
                Authentication token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(token);
            } catch (JwtException e) {
                request.setAttribute("exception", e.getMessage());
                /**
                 * 로그인 페이지로 리다이렉트 해주기.
                 */
            }
        }

        filterChain.doFilter(request, response);
    }


    public boolean validateToken(String token) {
        try {
            var encodeKey = Base64.getEncoder().encodeToString(key.getBytes());
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