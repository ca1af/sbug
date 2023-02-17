package com.sparta.sbug.channel.entity;

import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.thread.entity.Thread;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
public class Channel extends Timestamp {
    /**
     * 컬럼
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String channelName;
    @Column(nullable = false)
    @Setter
    private boolean inUse = true;


    /**
     * 생성자
     *
     * @param channelName 채널 이름
     */
    @Builder
    public Channel( String channelName) {
        this.channelName = channelName;
    }

    /**
     * 연관관계
     * channel : thread = 1 : N 양방향 연관 관계
     */
    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Thread> threads = new LinkedHashSet<>();

    /**
     * 연관관계 편의 메소드
     * addThread : channel = thread 편의 메소드
     */
    public void addThread(Thread thread) {
        this.threads.add(thread);
    }


    /**
     * 서비스 메소드
     */
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

}
