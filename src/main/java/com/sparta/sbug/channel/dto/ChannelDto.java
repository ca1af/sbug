package com.sparta.sbug.channel.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "채널명은 완성형 한글이나 영문, 숫자로 이루어져야 하고, 2~20글자여야 합니다.")
        private String channelName;
    }
}
