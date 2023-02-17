package com.sparta.sbug.comment.entity;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.emoji.entity.CommentEmoji;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

// lombok
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter

// jpa
@Entity
public class Comment extends Timestamp {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Setter
    private boolean inUse = true;

    /**
     * 생성자
     *
     * @param content 내용
     * @param user    댓글을 남긴 사용자
     */
    @Builder
    public Comment(String content, User user) {
        this.content = content;
        this.user = user;
    }


    /**
     * 연관관계
     * comment : comment_emoji = 1:N 양방향 연관 관계
     * comment : thread = N:1 양방향 연관 관계
     * comment : user = N:1 단방향 연관 관계
     */
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<CommentEmoji> emojis = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;
    /**
     * 연관관계 편의 메소드
     * setThread : comment - thread
     * addEmoji : comment - emoji
     */
    public void setThread(Thread thread) {
        this.thread = thread;
        this.channel = thread.getChannel();
        thread.addComment(this);
    }

    /**
     * 연관관계 편의 메소드
     */
    public void addEmoji(CommentEmoji emoji) {
        this.emojis.add(emoji);
    }

    /**
     * 서비스 메소드
     */
    public void updateContent(String content) {
        this.content = content;
    }

}
