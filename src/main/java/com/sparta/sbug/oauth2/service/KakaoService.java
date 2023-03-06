package com.sparta.sbug.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.sbug.oauth2.dto.KakaoUserInfo;
import com.sparta.sbug.security.dto.TokenResponseDto;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final static String REST_API_KEY = "1ef0cf0c88ad6600843f2240a8dbc8b5";
    private final static String REDIRECT_URI = "http://sbug-front-server-s3.s3.ap-northeast-2.amazonaws.com/frontdoor.html";

    /**
     * 작동 방식은 다음과 같습니다.
     * 1. 카카오에서 정해준 형식의 code로 인가 요청을 한다. 응답 받는 부분은 RestTemplate~ 부분.
     * 2. 인가 요청을 할 시에 카카오에서는 액세스 토큰을 발급한다.
     * 3. 그 엑세스 토큰 안에 있는 사용자의 정보를 파싱해서 nickname, email을 가져온다.
     * 4. 우리가 만든 JwtProvider 안에 있는 토큰 생성 로직을 거쳐 atk, rtk를 발급한다.
     * 5. 카카오 로그인 시에 userRepository 에서 카카오 이메일과 같은 이메일이 없다면, 새로 회원 가입을 한다.
     * 6. 모든 요청이 완료되면 controller 에서 응답값으로 atk, rtk 를 Json 형식으로 응답한다.
     * 7. 발급한 token 을 프론트단에서 사용자의 header 등에 넣고 사용한다.
     */
    public TokenResponseDto kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        registerKakaoUserIfNeeded(kakaoUserInfo);

        return jwtProvider.createTokenKakao(kakaoUserInfo.getEmail());
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    //인가 코드
    public String getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new KakaoUserInfo(id, nickname, email);
    }

    // 3. 필요시에 회원가입
    @Transactional
    public void registerKakaoUserIfNeeded(KakaoUserInfo kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        Optional<User> optionalUser = userRepository.findByKakaoIdAndInUseIsTrue(kakaoId);
        User kakaoUser;
        if (optionalUser.isEmpty()) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            Optional<User> optionalEmailUser = userRepository.findByEmailAndInUseIsTrue(kakaoEmail);
            if (optionalEmailUser.isPresent()) {
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = optionalEmailUser.get().kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = passwordEncoder.encode(UUID.randomUUID().toString());

                kakaoUser = User.builder().nickname(kakaoUserInfo.getNickname())
                        .email(kakaoUserInfo.getEmail())
                        .password(password).build();
                kakaoUser.kakaoIdUpdate(kakaoUserInfo.getId());
                kakaoUser.setProfileImage("cat.png");
            }

            userRepository.save(kakaoUser);
        }
    }
}
