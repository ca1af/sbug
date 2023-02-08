package com.sparta.sbug.channel.dto;

import com.sparta.sbug.channel.entity.Channel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ChannelResponseDto {
    private String channelName;

    private ChannelResponseDto(Channel channel) {
        this.channelName = channel.getChannelName();
    }

    public static ChannelResponseDto of(Channel channel){
        return new ChannelResponseDto(channel);
    }
}
