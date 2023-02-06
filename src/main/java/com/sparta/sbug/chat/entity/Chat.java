package com.sparta.sbug.chat.entity;

import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.userChatRoom.entity.UserChatRoom;
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
public class Chat extends Timestamp {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String message;

    /**
     * 생성자
     */
    @Builder
    public Chat(String message) {
        this.message = message;
    }

    /**
     * 연관관계
     */
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "chat_room_id", referencedColumnName = "chat_room_id"),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id")}
    )
    private UserChatRoom userChatRoom;

    /**
     * 연관관계 편의 메소드
     */


    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
}
