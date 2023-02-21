package com.sparta.sbug.websocket.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class HandShakeHandler implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        final HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
        CsrfToken token = (CsrfToken) httpRequest.getAttribute(CsrfToken.class.getName());
        if (token == null) {
            return true;
        }

        token = new DefaultCsrfToken(token.getHeaderName(), token.getParameterName(), token.getToken());
        attributes.put(CsrfToken.class.getName(), token);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
