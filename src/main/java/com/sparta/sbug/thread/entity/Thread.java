package com.sparta.sbug.thread.entity;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.comment.entity.Comment;
import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.emoji.entity.ThreadEmoji;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Thread extends Timestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "thread", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "thread", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ThreadEmoji> emojis = new LinkedHashSet<>();

    @Builder
    public Thread(Channel channel, User user, String requestContent){
        this.channel = channel;
        this.user = user;
        this.content = requestContent;
    }


    public void updateThread(String requestContent){this.content = requestContent;}
}
