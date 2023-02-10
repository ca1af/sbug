package com.sparta.sbug.channel.dto;

import lombok.Data;

public class ChannelDto {

    @Data
    public static class InvitationRequest {
        private String email;
    }

    @Data
    public static class ChannelRequest {
        private String channelName;
    }
}
