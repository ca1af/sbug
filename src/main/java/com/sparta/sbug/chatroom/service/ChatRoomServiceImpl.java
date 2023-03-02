package com.sparta.sbug.chatroom.service;

import com.sparta.sbug.aws.service.S3Service;
import com.sparta.sbug.chatroom.dto.ChatRoomDto;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.chatroom.repository.ChatRoomRepository;
import com.sparta.sbug.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.util.List;

// lombok
@RequiredArgsConstructor

// springframework stereotype
@Service

// springframework transaction
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    // for S3
    private final S3Service s3Service;

    @Override
    public Long createChatRoom(User user1, User user2) {
        ChatRoom chatRoom = ChatRoom.builder().user1(user1).user2(user2).build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getId();
    }

    @Override
    public Long getChatRoomIdOrNegative(User user1, User user2) {
        return chatRoomRepository.findChatRoomIdByUsers(user1.getId(), user2.getId());
    }

    @Override
    public ChatRoom getChatRoomById(Long id) {
        return chatRoomRepository.findById(id).orElseThrow();
    }

    @Override
    public List<ChatRoomDto> getChatRoomList(User user) {
        List<ChatRoomDto> chatRoomDtoList = chatRoomRepository.getChatRoomListByUserId(user.getId());
        S3Presigner preSigner = s3Service.getPreSigner();
        for (ChatRoomDto chatRoomDto : chatRoomDtoList) {
            chatRoomDto.setProfileImage(s3Service.getObjectPreSignedUrl(s3Service.bucketName, chatRoomDto.getProfileImage(), preSigner));
        }
        preSigner.close();
        return chatRoomRepository.getChatRoomListByUserId(user.getId());
    }
}