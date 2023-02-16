package com.sparta.sbug.thread.entity;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.user.entity.User;
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
public class Thread extends Timestamp {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    /**
     * 생성자
     */
    @Builder
    public Thread(Channel channel, User user, String requestContent){
        this.channel = channel;
        this.user = user;
        this.content = requestContent;
    }

    /**
     * 연관관계
     * thread : user = N : 1 단방향 연관관계
     * thread : channel = N : 1 양방향 연관관계
     * thread : comment = 1 : N 양방향 연관관계
     * thread : thread_emoji = 1 : N 양방향 연관관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "thread", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "thread", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<ThreadEmoji> emojis = new LinkedHashSet<>();

    /**
     * 연관관계 편의 메소드
     * addChannel : thread - comment 편의 메소드
     * addEmoji : thread - thread_emoji 편의 메소드
     */
    public void addChannel(Comment comment) {
        this.comments.add(comment);
    }

    public void addEmoji(ThreadEmoji threadEmoji) {
        this.emojis.add(threadEmoji);
    }

    /**
     * 서비스 메소드
     */
    // Thread 수정 메소드
    public void changeThread(String requestContent){this.content = requestContent;}









    public void updateThread(String requestContent){this.content = requestContent;}
}
