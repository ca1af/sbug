package com.sparta.sbug.websocket.handler;

import com.sparta.sbug.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    // 전송 받은 메세지에서 JWT 토큰을 검증하는 메서드
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // 헤더 토큰 열기
        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));

        // 토큰 자르기
        if(authorizationHeader == null || authorizationHeader.equals("null")) {
            throw new MessageDeliveryException("메세지: 토큰 없음");
        }

        String token = authorizationHeader.substring("Bearer ".length());

        // 토큰 인증
        if (!jwtUtil.validateToken(token, null)) {
            throw new MessageDeliveryException("메세지 토큰 예외");
        }

        return message;
    }

}
