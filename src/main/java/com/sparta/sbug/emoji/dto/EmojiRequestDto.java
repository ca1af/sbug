package com.sparta.sbug.emoji.dto;

import com.sparta.sbug.emoji.entity.EmojiType;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이모지 반응 요청 DTO
 */
// lombok
@Getter
@NoArgsConstructor
public class EmojiRequestDto {

    private EmojiType emojiType;

}
