package com.sparta.sbug.userchannel.enttiy;

import com.sparta.sbug.channel.entity.Channel;
import com.sparta.sbug.common.entity.Timestamp;
import com.sparta.sbug.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserChannel extends Timestamp {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "channel_id")
    Channel channel;
    @Builder
    public UserChannel(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
    }

}
