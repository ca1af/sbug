package com.sparta.sbug.channel.dto;

import com.sparta.sbug.channel.entity.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelResponseDto {
    private String channelName;
    private Long channelId;

    private ChannelResponseDto(Channel channel) {
        this.channelName = channel.getChannelName();
        this.channelId = channel.getId();
    }

    public static ChannelResponseDto of(Channel channel){
        return new ChannelResponseDto(channel);
    }
}
