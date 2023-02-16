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

// springframework context
@Configuration

// springframework web socket
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static final String ENDPOINT = "/stomp/unit";
    public static final String TOPIC = "/topic";
    public static final String PREFIX = "/app";

    private final ChatPreHandler chatPreHandler;

    /**
     * WebSocket 또는 SockJS가 웹소켓 핸드셰이크 커넥션을 생성할 경로를 정의
     *
     * @param registry 스톰프 엔드포인트 설정
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint(ENDPOINT)
                // CORS 설정
                .setAllowedOriginPatterns("*")
                // SockJS 사용
                .withSockJS();
    }

    /**
     * 클라이언트가 구독(Subscribe)할 경로와 전송(Publish)할 경로를 정의
     *
     * @param config 메세지 브로커 설정
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트에서 요청을 전송할 경로 (이 경로 뒤에 컨트롤러의 매핑 주소가 붙음)
        config.setApplicationDestinationPrefixes(PREFIX);
        // 이 경로를 Subscribe 하고 있는 클라이언트에게 메세지를 전달하는 작업을 수행하게 됨
        config.enableSimpleBroker(TOPIC);
    }

    /**
     * 채널 인터셉터를 설정하기 위한 메서드
     *
     * @param registration 채널 설정
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 이 설정으로 핸들러가 메세지를 중간에 가로채게 됨
        registration.interceptors(chatPreHandler);
    }

}
