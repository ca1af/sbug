package com.sparta.sbug.channel.dto;

import com.sparta.sbug.channel.entity.Channel;
import lombok.Getter;
import lombok.Setter;
/**
 * 채널 반환 DTO
 */
// lombok
@Getter
@Setter
public class ChannelResponseDto {

    private Long id;
    private String channelName;
    private Long channelId;

    /**
     * 생성자
     */
    private ChannelResponseDto(Channel channel) {
        this.id = channel.getId();
        this.channelName = channel.getChannelName();
        this.channelId = channel.getId();
    }

    public static ChannelResponseDto of(Channel channel) {
        return new ChannelResponseDto(channel);
    }
}
