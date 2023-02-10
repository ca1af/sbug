package com.sparta.sbug.websocket.configuration;

import com.sparta.sbug.websocket.handler.ChatPreHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// lombok
@RequiredArgsConstructor

// springframework
@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String ENDPOINT = "/stomp/unit";
    public static final String TOPIC = "/topic";
    public static final String PREFIX = "/app";

    private final ChatPreHandler chatPreHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 또는 SockJS가 웹소켓 핸드셰이크 커넥션을 생성할 경로
        registry.addEndpoint(ENDPOINT)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트에서 전송 요청을 처리하는 경로
        config.setApplicationDestinationPrefixes(PREFIX);
        // 이 경로를 Subscribe 하고 있는 클라이언트에게 메세지를 전달하는 작업을 수행
        config.enableSimpleBroker(TOPIC);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(chatPreHandler);
    }

}
