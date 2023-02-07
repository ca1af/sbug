package com.sparta.sbug.chatroom.entity;

import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.user.entity.User;
import com.sparta.sbug.userchatroom.entity.UserChatRoom;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

// lombok
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter

// jpa
@Entity
public class ChatRoom extends Timestamp {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String chatRoomName;

    /**
     * 생성자
     */
    @Builder
    public ChatRoom(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    /**
     * 연관관계
     */
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    Set<UserChatRoom> userChatRooms = new LinkedHashSet<>();

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */
    public void addUser(User user) {
        UserChatRoom userChatRoom = UserChatRoom.builder().chatRoom(this).user(user).build();
        userChatRooms.add(userChatRoom);
        user.getUserChatRooms().add(userChatRoom);
    }


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
}
