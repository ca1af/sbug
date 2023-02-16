package com.sparta.sbug.emoji.entity;

import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

// lombok
@NoArgsConstructor
@Getter

// jpa
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Emoji {

    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private EmojiType emojiType;

    /**
     * 생성자
     *
     * @param emojiType 이모지 종류
     * @param user      반응한 사용자
     */
    public Emoji(String emojiType, User user) {
        this.emojiType = EmojiType.valueOf(emojiType);
        this.user = user;
    }

    /**
     * 연관관계
     * emoji : user = N:1 단방향 연관 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
