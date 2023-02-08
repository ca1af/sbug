package com.sparta.sbug.emoji.entity;

import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor
@Getter
public abstract class Emoji {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private EmojiType emojiType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Emoji(EmojiType emojiType, User user){
        this.emojiType = emojiType;
        this.user = user;
    }
}
