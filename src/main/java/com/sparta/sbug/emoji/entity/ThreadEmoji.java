package com.sparta.sbug.emoji.entity;

import com.sparta.sbug.comment.Comment;
import com.sparta.sbug.thread.Thread;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("Thread")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThreadEmoji extends Emoji{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    private Thread thread;
}
