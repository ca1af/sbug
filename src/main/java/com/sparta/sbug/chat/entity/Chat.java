package com.sparta.sbug.chat.entity;

import com.sparta.sbug.chatroom.entity.ChatRoom;
import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
public class Chat extends Timestamp {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private ChatStatus status = ChatStatus.NEW;

    /**
     * 생성자
     */
    @Builder
    public Chat(ChatRoom room, User sender, String message, User receiver) {
        this.room = room;
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
    }

    /**
     * 연관관계
     */
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void updateMessage(String message) {
        this.message = message;
    }

}
