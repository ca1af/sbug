package com.sparta.sbug.channel.entity;

import com.sparta.sbug.channel.dto.ChannelRequestDto;
import com.sparta.sbug.thread.entity.Thread;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String channelName;

    @OneToMany(mappedBy = "channel")
    private Set<Thread> threads = new LinkedHashSet<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String adminEmail;

    @Builder
    public Channel(User user, String adminEmail, String channelName) {
        this.user = user;
        this.adminEmail = adminEmail;
        this.channelName = channelName;
    }

    public void updateChannel(Channel channel, User user, String channelName) {
        this.channelName = channelName;
    }

    // 연관관계 편의 매서드
    public void addThread(Thread thread) {
        this.threads.add(thread);
    }

}
