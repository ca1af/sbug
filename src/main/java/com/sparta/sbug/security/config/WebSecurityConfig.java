package com.sparta.sbug.security.config;

import com.sparta.sbug.security.exception.CustomAccessDeniedHandler;
import com.sparta.sbug.security.exception.CustomAuthenticationEntryPoint;
import com.sparta.sbug.security.jwt.JwtAuthFilter;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.security.userDetails.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// lombok
@RequiredArgsConstructor

// springframework context
@Configuration

// springframework security
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableMethodSecurity // 위 어노테이션은 Deprecated

// springframework scheduling
@EnableScheduling // @Scheduled 어노테이션 활성화
public class WebSecurityConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * PasswordEncoder를 빈으로 주입
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * web.ignoring() 설정으로 필터 적용을 제외해줄 요청을 정의합니다.
     *
     * @return WebSecurityCustomizer
     */
    // 가장 먼저 시큐리티를 사용하기 위해선 선언해준다.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /**
     * HttpSecurity에 대한 보안 설정들을 정의합니다.
     *
     * @param http : HttpSecurity
     * @return SecurityFilterChain
     * @throws Exception 발생 가능한 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests()
                .requestMatchers("/stomp/**").permitAll()
                .requestMatchers("/chat/**").permitAll()
                .requestMatchers("/api/users/sign-up").permitAll()
                .requestMatchers("/api/users/login").permitAll()
                .requestMatchers("/api/users/kakao**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtAuthFilter(jwtProvider, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(customAccessDeniedHandler);
    //ArithmeticException<>
        return http.build();
    }

    /**
     * 다른 출처의 자원들을 공유할 수 있도록 CORS 설정을 정의합니다.
     *
     * @param registry : CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .exposedHeaders("Authorization")
                .exposedHeaders("RTK");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}