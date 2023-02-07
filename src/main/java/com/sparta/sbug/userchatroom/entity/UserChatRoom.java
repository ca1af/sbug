package com.sparta.sbug.userchatroom.entity;
import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
//@IdClass(UserChatRoomId.class)
public class UserChatRoom {
    /**
     * 생성자
     */
    @Builder
    public UserChatRoom(ChatRoom chatRoom, User user) {
        this.chatRoom = chatRoom;
        this.user = user;
    }

    /**
     * 연관 관계
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
