package com.sparta.sbug.emoji.entity;

import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@DiscriminatorValue("THREAD")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThreadEmoji extends Emoji{
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thread_id")
    private Thread thread;


    public ThreadEmoji(String emojiType, User user, Thread thread){
        super(emojiType, user);
        this.thread = thread;
    }
}