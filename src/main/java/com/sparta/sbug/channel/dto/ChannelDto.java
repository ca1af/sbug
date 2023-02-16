package com.sparta.sbug.channel.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 채널 DTO
 */
public class ChannelDto {


    /**
     * 초대 요청 DTO
     */
    @Data
    @NoArgsConstructor
    public static class InvitationRequest {
        @NotNull(message = "초대할 사용자의 이메일을 입력해주세요")
        private String email;
    }

    /**
     * 채널 생성 or 수정 요청 DTO
     */
    @Data
    @NoArgsConstructor
    public static class ChannelRequest {
        @NotNull(message = "채널 이름을 입력해주세요")
        private String channelName;
    }
}
