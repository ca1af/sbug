package com.sparta.sbug.websocket.handler;

import com.sparta.sbug.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.*;

// lombok
@RequiredArgsConstructor
@Slf4j

// springframework stereotype
@Component
public class ChatPreHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private static final Map<String, String> SESSION_EMAIL_MAP = new HashMap<>();
    private static final Map<String, String> USER_CHAT_ROOM_MAP = new HashMap<>();
    public static final Map<String, Set<String>> CHAT_ROOM_USER_MAP = new HashMap<>();

    /**
     * 보내진 메세지를 전처리하는 메서드
     * 전송 받은 메세지에서 JWT 토큰을 검증하기 위해 사용합니다.
     *
     * @param message 보내진 메세지
     * @param channel 메세지 채널
     * @return Message 전처리 후 메세지를 다시 반환함
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        // 연결 해제 메세지 일 때
        if (Objects.requireNonNull(headerAccessor.getMessageType()).equals(SimpMessageType.DISCONNECT)) {
            handleWhenDisconnect(headerAccessor);
            return message;
        }

        // 헤더에서 토큰을 가져옴
        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader("Authorization"));

        if (authorizationHeader == null || authorizationHeader.equals("null")) {
            throw new MessageDeliveryException("메세지: 토큰 없음");
        }

        String token = authorizationHeader.substring("Bearer ".length());

        // 토큰 인증
        if (!jwtProvider.validateMessageToken(token)) {
            throw new MessageDeliveryException("메세지 토큰 예외");
        }

        // 연결 메세지 일 때
        if (headerAccessor.getMessageType().equals(SimpMessageType.CONNECT)) {
            handleWhenConnect(headerAccessor, token);
        } else if (headerAccessor.getMessageType().equals(SimpMessageType.SUBSCRIBE)) {
            handleWhenSubscribe(headerAccessor);
        } else if (headerAccessor.getMessageType().equals(SimpMessageType.UNSUBSCRIBE)) {
            handleWhenUnsubscribe(headerAccessor);
        }

        return message;
    }

    private void handleWhenConnect(StompHeaderAccessor headerAccessor, String token) {
        var email = jwtProvider.getSubject(token);
        var sessionId = headerAccessor.getSessionId();
        SESSION_EMAIL_MAP.put(sessionId, email);
        log.info(String.format("[Chat] %s(%s) CONNECT", email, sessionId));
    }

    private void handleWhenUnsubscribe(StompHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String email = SESSION_EMAIL_MAP.get(sessionId);

        if (USER_CHAT_ROOM_MAP.containsKey(sessionId)) {
            String chatRoom = USER_CHAT_ROOM_MAP.get(sessionId);
            CHAT_ROOM_USER_MAP.get(chatRoom).remove(sessionId);
            USER_CHAT_ROOM_MAP.remove(sessionId);
            log.info(String.format("[Chat] %s(%s) UNSUBSCRIBE IN %s", email, sessionId, chatRoom));
        }
    }

    private void handleWhenSubscribe(StompHeaderAccessor headerAccessor) {
        String destination = headerAccessor.getDestination();
        String sessionId = headerAccessor.getSessionId();
        String email = SESSION_EMAIL_MAP.get(sessionId);

        if (CHAT_ROOM_USER_MAP.containsKey(destination)) {
            Set<String> chatRoom = CHAT_ROOM_USER_MAP.get(destination);
            chatRoom.add(sessionId);
        } else {
            Set<String> chatRoom = new HashSet<>();
            chatRoom.add(sessionId);
            CHAT_ROOM_USER_MAP.put(destination, chatRoom);
        }

        USER_CHAT_ROOM_MAP.put(sessionId, destination);
        log.info(String.format("[Chat] %s(%s) SUBSCRIBE IN %s", email, sessionId, destination));
    }

    private void handleWhenDisconnect(StompHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String email = SESSION_EMAIL_MAP.get(sessionId);
        handleWhenUnsubscribe(headerAccessor);

        SESSION_EMAIL_MAP.remove(sessionId);
        log.info(String.format("[Chat] %s(%s) DISCONNECT", email, sessionId));
    }

}
