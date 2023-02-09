package com.sparta.sbug.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChatHandler extends TextWebSocketHandler {

    // 세션을 저장한 리스트
    private final static List<WebSocketSession> list = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // payload는 Header, META 데이터, 에러 체크 비트를 제외한 전송되는 순수한 데이터를 의미
        String payload = message.getPayload();
        System.out.println("payload : " + payload);

        for (WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    // 유저가 접속 시 호출되는 메서드
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        list.add(session);
        System.out.println(session + " 접속");
    }

    // 유저 접속 해제 시 호출되는 메서드
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session + " 클라이언트 접속 해제");
        list.remove(session);
    }
}
