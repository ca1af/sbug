package com.sparta.sbug.chat.controller;

import com.sparta.sbug.chat.dto.ChatRequestDto;
import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.service.ChatService;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.chatroom.service.ChatRoomService;
import com.sparta.sbug.security.jwt.JwtProvider;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

// lombok
@RequiredArgsConstructor

// springframework web bind
@RestController
public class ChatMessageController {

    private final ChatService chatService;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate template;
    private final JwtProvider jwtProvider;

    /**
     * 클라이언트(유저)가 보낸 메세지를 받아 처리하는 메소드입니다.
     * 다음과 같은 순서로 동작합니다.
     * 1. 먼저 메세지(Chat)를 만들기 위해 다음 과정을 진행합니다.
     *      a. 전달 받은 DTO 내 수신자 ID를 이용해 수신자(User)를 찾습니다.
     *      b. 송신자(=요청자, User)을 찾기 위해 헤더로 전달 받은 JWT 토큰에서 요청자의 ID를 찾습니다.
     *      c. 요청자의 ID로 송신자(User)를 찾습니다.
     *      d. DTO 내 채팅방 ID를 이용해 송신자와 수신자가 구독 중인 채팅방을 찾습니다.
     *      e. Chat Service의 메서드를 이용해 메세지를 저장하고 응답 DTO를 받습니다.
     * 2. SimpMessagingTemplate를 이용해 응답 DTO를 사용자들이 구독 중인 토픽으로 보냅니다.
     * [MESSAGE] app/chats
     *
     * @param requestDto : 방 ID, 수신자 ID, 메세지 내용
     * @param rawToken   : Header의 Authorization의 값
     */
    @MessageMapping("/chats")
    public void sendMessage(ChatRequestDto requestDto, @Header("Authorization") String rawToken) {
        // receiver
        User receiver = userService.getUserById(requestDto.getReceiverId());

        // sender
        String email = jwtProvider.getSubject(rawToken.substring(7));
        User sender = userService.getUser(email);

        // room
        ChatRoom room = chatRoomService.getChatRoomById(requestDto.getRoomId());

        /* responseDto : id(메세지 ID), sender(보낸 사람 닉네임), receiver(받는 사람 닉네임), message(내용),
                       receiverId(받는 사람 ID), status(이미 읽은 메세지인지 상태) */
        ChatResponseDto responseDto = chatService.createMessage(room, sender, receiver, requestDto.getMessage());
        template.convertAndSend("/topic/chats/rooms/" + room.getId(), responseDto);
    }
}
