package com.sparta.sbug.websocket.handler;

import com.sparta.sbug.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private static final Map<String, String> sessions = new HashMap<>();

    // 전송 받은 메세지에서 JWT 토큰을 검증하는 메서드
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 헤더 토큰 열기
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (Objects.requireNonNull(headerAccessor.getMessageType()).equals(SimpMessageType.DISCONNECT)) {
            var sessionId = headerAccessor.getSessionId();
            String email = sessions.get(sessionId);
            System.out.printf("[Chat] %s : %s DISCONNECT%s", email, sessionId, System.lineSeparator());
            return message;
        }

        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));

        // 토큰 자르기
        if(authorizationHeader == null || authorizationHeader.equals("null")) {
            throw new MessageDeliveryException("메세지: 토큰 없음");
        }

        String token = authorizationHeader.substring("Bearer ".length());

//         토큰 인증
//        if (!jwtProvider.validateToken(token, null)) {
//            throw new MessageDeliveryException("메세지 토큰 예외");
//        }

        if (Objects.requireNonNull(headerAccessor.getMessageType()).equals(SimpMessageType.CONNECT)) {
            Claims info = jwtProvider.getUserInfoFromToken(token);
            var email = info.getSubject();
            var sessionId = headerAccessor.getSessionId();
            sessions.put(sessionId, email);
            System.out.printf("[Chat] %s : %s CONNECT%s", email, sessionId, System.lineSeparator());
            return message;
        }

        return message;
    }

}
