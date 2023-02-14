package com.sparta.sbug.channel.entity;

import com.sparta.sbug.thread.entity.Thread;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Pattern(regexp = "^[a-zA-Z0-9]{4,10}$", message = "영문이나 숫자로 이루어져야 하고, 4~10글자여야 합니다.")
    private String channelName;

    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Thread> threads = new LinkedHashSet<>();

    private String adminEmail;

    @Builder
    public Channel(String adminEmail, String channelName) {
        this.adminEmail = adminEmail;
        this.channelName = channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    // 연관관계 편의 매서드
    public void addThread(Thread thread) {
        this.threads.add(thread);
    }

}
