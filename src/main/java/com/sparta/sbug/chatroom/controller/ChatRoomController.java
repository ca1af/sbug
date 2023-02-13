package com.sparta.sbug.chatroom.controller;

import com.sparta.sbug.chat.dto.ChatResponseDto;
import com.sparta.sbug.chat.service.ChatService;
import com.sparta.sbug.chatroom.dto.ChatRoomResponseDto;
import com.sparta.sbug.chatroom.service.ChatRoomService;
import com.sparta.sbug.common.dto.PageDto;
import com.sparta.sbug.security.userDetails.UserDetailsImpl;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework
@RestController
@RequestMapping("/api/rooms")
public class ChatRoomController {

    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    /**
     * 채팅할 유저의 ID를 받아 그 유저와 이미 채팅 중인 채팅방이 존재하는지 확인합니다.
     * - 이미 채팅 방이 존재할 때   : 해당 채팅 방의 ID를 반환받고 이전에 채팅한 내역을 불러옵니다.
     * - 채팅 방이 존재하지 않을 때 : 새로 채팅 방을 만들고 그 ID를 반환합니다.
     *
     * @param id          : 상대방의 ID
     * @param userDetails : 요청자 정보
     */
    @GetMapping("/enter/receivers/{id}")
    public ChatRoomResponseDto enter(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 채팅할 대상 유저를 찾아 가져옵니다.
        User user = userService.getUserById(id);

        // 채팅방의 존재 유무를 확인합니다.
        Long roomId = chatRoomService.getChatRoomIdOrNegative(user, userDetails.getUser());
        List<ChatResponseDto> chats = new ArrayList<>();

        // 채팅 방이 존재하지 않으면(채팅방 ID가 음수) 채팅 방을 만들고 그 ID를 반환 받습니다.
        if (roomId < 0) {
            roomId = chatRoomService.createChatRoom(user, userDetails.getUser());
        } else {
            // 채팅 방이 존재하면 이전에 채팅한 내역을 불러옵니다.
            PageDto pageDto = PageDto.builder().currentPage(1).size(5).sortBy("createdAt").build();
            chats = chatService.readAllMessageInChatRoom(userDetails.getUser().getId(), roomId, pageDto);
        }

        // 채팅방 ID와 채팅 내역을 반환합니다.
        return new ChatRoomResponseDto(roomId, chats);
    }
}
