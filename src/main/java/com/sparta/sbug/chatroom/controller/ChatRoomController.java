package com.sparta.sbug.chatroom.controller;

import com.sparta.sbug.chatroom.dto.ChatRoomDto;
import com.sparta.sbug.chatroom.service.ChatRoomService;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework web bind
@RestController
@RequestMapping("/api/rooms")
public class ChatRoomController {

    private final UserService userService;
    private final ChatRoomService chatRoomService;

    /**
     * 채팅할 유저의 ID를 받아 그 유저와 새로 채팅 방을 만들고 그 ID를 반환합니다.
     *
     * @param id          상대방의 ID
     * @param userDetails 요청자 정보
     * @return ChatRoomResponseDto
     */
    @GetMapping("/receivers/{id}")
    public Long enterNewRoom(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 채팅할 대상 유저를 찾아 가져옵니다.
        User user = userService.getUserById(id);
        Long roomId = chatRoomService.getChatRoomIdOrNegative(user, userDetails.getUser());
        if (roomId < 0) {
            roomId = chatRoomService.createChatRoom(user, userDetails.getUser());
        }

        // 채팅방 ID와 채팅 내역을 반환합니다.
        return roomId;
    }

    /**
     * 내가 속해있는 채팅방들의 리스트를 불러옵니다.
     * [GET] /api/rooms
     *
     * @param userDetails 요청자
     * @return List&lt;ChattingInfoResponseDto&gt;
     */
    @GetMapping("")
    public List<ChatRoomDto> getChatRoomList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatRoomService.getChatRoomList(userDetails.getUser());
    }

}
