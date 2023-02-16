package com.sparta.sbug.chatroom.entity;

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
public class ChatRoom {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 생성자
     *
     * @param user1 유저 1
     * @param user2 유저 2
     */
    @Builder
    public ChatRoom(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    /**
     * 연관관계
     * chatroom : user1 = N:1 단방향 연관 관계
     * chatroom : user2 = N:1 단방향 연관 관계
     */
    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

}
