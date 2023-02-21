package com.sparta.sbug.emoji.dto;

import com.sparta.sbug.emoji.entity.Emoji;
import com.sparta.sbug.emoji.entity.EmojiType;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이모지 반응 요청 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class EmojiResponseDto {
    private EmojiType emojiType;
    private Long userId;

    private EmojiResponseDto(Emoji emoji) {
        this.emojiType = emoji.getEmojiType();
        this.userId = emoji.getUser().getId();
    }

    public static EmojiResponseDto of(Emoji emoji) {
        return new EmojiResponseDto(emoji);
    }
}


