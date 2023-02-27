package com.sparta.sbug.userchannel.enttiy;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

// lombok
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

// jpa
@Entity
public class UserChannel extends Timestamp {

    /**
     * 컬럼
     */
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    @Setter
    private boolean inUse = true;

    /**
     * 생성자
     */
    @Builder
    public UserChannel(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
    }

    /**
     * 연관관계
     * user_channel : user = N : 1 단방향 연관관계
     * user_channel : channel = N : 1 단방향 연관관계
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "channel_id")
    Channel channel;

}
