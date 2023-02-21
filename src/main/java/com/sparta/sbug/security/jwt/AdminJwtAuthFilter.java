package com.sparta.sbug.security.jwt;

import com.sparta.sbug.security.userDetails.AdminDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AdminJwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final AdminDetailsServiceImpl adminDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("AdminToken");
        try {
            String adminToken = jwtProvider.getSubject(accessToken);
            String atk = accessToken.substring(7);
            if (adminToken != null) {
                String email = jwtProvider.getSubject(atk);
                UserDetails userDetails = adminDetailsService.loadUserByUsername(email);
                Authentication authentication = jwtProvider.createAuthentication(userDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}