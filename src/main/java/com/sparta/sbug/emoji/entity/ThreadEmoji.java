package com.sparta.sbug.emoji.entity;

import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
@DiscriminatorValue("THREAD")
public class ThreadEmoji extends Emoji{

    /**
     * 생성자
     */
    public ThreadEmoji(String emojiType, User user, Thread thread){
        super(emojiType, user);
        this.thread = thread;
    }

    /**
     * 연관관계
     * thread_emoji : thread = N:1 양방향 연관 관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    private Thread thread;

    /**
     * 연관관계 편의 메소드
     * setThread : thread_emoji - thread
     */
    public void setThread(Thread thread) {
        this.thread = thread;
        thread.addEmoji(this);
    }

}
