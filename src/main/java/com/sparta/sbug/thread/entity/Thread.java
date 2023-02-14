package com.sparta.sbug.thread.entity;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.thread.dto.ThreadRequestDto;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    public Thread(Channel channel, User user, String requestContent){
        this.channel = channel;
        this.user = user;
        this.content = requestContent;
    }

    // Thread 수정 메소드
    public void changeThread(String requestContent){this.content = requestContent;}
}
